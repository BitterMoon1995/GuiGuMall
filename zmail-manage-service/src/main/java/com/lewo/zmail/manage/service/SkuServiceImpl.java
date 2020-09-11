package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.config.LuaConfig;
import com.lewo.zmail.manage.dao.PmsSkuAttrValueMapper;
import com.lewo.zmail.manage.dao.PmsSkuImageMapper;
import com.lewo.zmail.manage.dao.PmsSkuInfoMapper;
import com.lewo.zmail.manage.dao.PmsSkuSaleAttrValueMapper;
import com.lewo.zmall.model.PmsSkuAttrValue;
import com.lewo.zmall.model.PmsSkuImage;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.model.PmsSkuSaleAttrValue;
import com.lewo.zmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.ResponseEntity;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@DubboService
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuAttrValueMapper baseAttrValueMapper;
    @Autowired
    PmsSkuInfoMapper skuInfoMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper saleAttrValueMapper;
    @Autowired
    PmsSkuImageMapper skuImageMapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    DefaultRedisScript<Long> skuLock;


    @Override
    public void saveSkuInfo(PmsSkuInfo skuInfo) {
        //基本信息
        skuInfoMapper.insert(skuInfo);
        //得到存入后的ID
        List<PmsSkuInfo> vars = skuInfoMapper.select(skuInfo);
        if (vars.size() == 0) return;
        PmsSkuInfo var = vars.get(0);
        System.out.println(var);
        String skuId = var.getId();

        //base平台属性
        List<PmsSkuAttrValue> baseAttrValueList = skuInfo.getSkuAttrValueList();
        baseAttrValueList.forEach(baseAttr -> {
            baseAttr.setSkuId(skuId);
            baseAttrValueMapper.insert(baseAttr);
        });
        //sku销售属性
        List<PmsSkuSaleAttrValue> saleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        saleAttrValueList.forEach(saleAttr -> {
            saleAttr.setSkuId(skuId);
            saleAttrValueMapper.insert(saleAttr);
        });
        //图片
        List<PmsSkuImage> imageList = skuInfo.getSkuImageList();
        imageList.forEach(img -> {
            img.setSkuId(skuId);
            skuImageMapper.insert(img);
        });
    }

    @Override
//    @Cacheable(cacheNames = {"sku"})
    //不用注解，亲自下场，亲自解决穿透和击穿的问题
    public PmsSkuInfo getById(String skuId) throws InterruptedException {
        ValueOperations<String, Object> op = redisTemplate.opsForValue();
        PmsSkuInfo info = (PmsSkuInfo) op.get(skuId);
        //缓存无值
        if (info == null) {
            System.out.println("缓存没有");

            //★小问题一：拿到锁的线程XiaoChuan突然暴毙了，且超过了锁的过期时间
            //这时候另外一个线程LiGan刚拿到锁，请求DB，这时候XiaoChuan线程又亲妈复活了
            //XiaoChuan复活后继续执行代码并删除了LiGan的锁(后果未知，至少这里无所谓)
            String uniqueLock = UUID.randomUUID().toString().substring(0, 6);

            //★互斥锁防缓存击穿★，
            // 这里表示的是”skuId“与”skuId_mutex“两个键是互斥的，有你没我
            //★也是Redis两种分布式锁解决方案之一（另一个是redission）
            // 只有skuId_mutex这个键第一次被赋值的时候，才会执行请求DB的代码
            // 也就是在热点K-V过期的情况下，保证分布式环境下DB层绝对只会被请求一次
            if (op.setIfAbsent(skuId+"_mutex",uniqueLock,3*6, TimeUnit.SECONDS)) {
                System.out.println("拿到锁，走DB");
                PmsSkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
                //数据库无此值
                if (skuInfo == null) {
                    System.out.println("没查到，设置自定义空值");
                    //★存null值防缓存穿透★
                    //工业思路：增加校验，比如用户鉴权校验，参数做校验，不合法的参数直接代码Return，
                    //比如：id 做基础校验，id <=0的直接拦截等。
                    PmsSkuInfo nullInfo = new PmsSkuInfo();
                    nullInfo.setId("noValue");
                    op.set(skuId,nullInfo,30,TimeUnit.MINUTES);
                    return null;
                }
                //数据库有值
                else {
                    System.out.println("查到，设置值");
                    getDetail(skuInfo,skuId);
                    op.set(skuId,skuInfo,30,TimeUnit.MINUTES);
                    System.out.println("释放锁");
                    //要确保删的是自己的锁，只能删自己的锁。
                    // Java方式：（不保证原子性）
//                    String thisLock = (String) op.get(skuId+"_mutex");
//                    if (StringUtils.isNotBlank(thisLock) && thisLock.equals(uniqueLock))
//                    redisTemplate.delete(skuId+"_mutex");

                    //★小问题二：在锁值判断的时候锁过期了（卧槽，恶俗啊）
                    //高雅之lua脚本（保证原子性、速度）
                    List<String> keys = Collections.singletonList(skuId + "_mutex");
                    Long scriptRes = redisTemplate.execute(skuLock, keys, uniqueLock);
                    assert scriptRes != null;
                    ResponseEntity
                            .ok(scriptRes);
                    System.out.println(scriptRes);

                    return skuInfo;
                }
            }
            //★拿不到锁，休眠，再尝试(自旋)。保证永远只有一个线程能走一次DB，MySQL就永远不会被打死y1s1hxd
            else {
                System.out.println("未拿到互斥锁，休眠50毫秒再请求");
                sleep(50);
                return getById(skuId);
            }
        }

        else {
            System.out.println("缓存有");
            return info;
        }
    }
    private void getDetail(PmsSkuInfo skuInfo, String skuId) {
//        PmsSkuAttrValue attrValue = new PmsSkuAttrValue();
//        attrValue.setSkuId(skuId);
//        List<PmsSkuAttrValue> attrValues = baseAttrValueMapper.select(attrValue);
//        skuInfo.setSkuAttrValueList(attrValues);

        PmsSkuImage skuImage = new PmsSkuImage();
        skuImage.setSkuId(skuId);
        List<PmsSkuImage> skuImages = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImages);

    }

    @Override
    public void delSku(String skuId) {
        skuInfoMapper.deleteByPrimaryKey(skuId);

        PmsSkuImage skuImage = new PmsSkuImage();
        skuImage.setSkuId(skuId);
        skuImageMapper.delete(skuImage);

        PmsSkuAttrValue baseAttr = new PmsSkuAttrValue();
        baseAttr.setSkuId(skuId);
        baseAttrValueMapper.delete(baseAttr);

        PmsSkuSaleAttrValue saleAttr = new PmsSkuSaleAttrValue();
        saleAttr.setSkuId(skuId);
        saleAttrValueMapper.delete(saleAttr);
    }

    @Override
    public List<PmsSkuInfo> getAllSkuBySpuId(String spuId) {
        PmsSkuInfo skuInfo = new PmsSkuInfo();
        skuInfo.setSpuId(spuId);
        return skuInfoMapper.select(skuInfo);
    }

    @Override
    public List<PmsSkuSaleAttrValue> getSaleAttrValue(String skuId) {
        PmsSkuSaleAttrValue saleAttrValue = new PmsSkuSaleAttrValue();
        saleAttrValue.setSkuId(skuId);
        return saleAttrValueMapper.select(saleAttrValue);
    }

    @Override
    @Cacheable(cacheNames = {"skuAttrMap"})
    public Map<String, String> generateSkuMap(String spuId) {
        //根据spuId查询旗下的所有的SKU
        List<PmsSkuInfo> skuInfos = getAllSkuBySpuId(spuId);
        //现在要拿到键为一个SKU所有销售属性值ID拼接的字符串，值为该skuID的map并返回给页面
        HashMap<String, String> skuMap = new HashMap<>();

        for (PmsSkuInfo sku : skuInfos
        ) {
            StringBuilder key = new StringBuilder();
            String value = sku.getId();
            //根据skuId查询旗下所有销售属性
            List<PmsSkuSaleAttrValue> attrValues = getSaleAttrValue(value);
            for (PmsSkuSaleAttrValue attrValue : attrValues
            ) {
                //将所有销售属性对应的SPU销售属性ID拼接成每个SKU的惟一串，作为key
                key.append(attrValue.getSaleAttrValueId()).append("|");
            }
            //value就是SKU的ID。这样前端切换销售属性时，就可以直接根据销售属性的组合在本地得到对应的SkuID，
            //而并不需要再专门查询一次，每一次切换属性就少了一次网络IO和数据库IO
            //key要去除最后一个‘|’
            skuMap.put(key.toString().substring(0, key.length() - 1), value);
        }
        return skuMap;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> skuInfos = skuInfoMapper.selectAll();

        skuInfos.forEach(skuInfo -> {
            String id = skuInfo.getId();
            PmsSkuAttrValue attrValue = new PmsSkuAttrValue();
            attrValue.setSkuId(id);
            List<PmsSkuAttrValue> attrValueList = baseAttrValueMapper.select(attrValue);
            skuInfo.setSkuAttrValueList(attrValueList);
        });

        return skuInfos;
    }
}
