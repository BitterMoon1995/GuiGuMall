package com.lewo.zmail.cart.service;

import com.alibaba.fastjson.JSON;
import com.lewo.zmail.cart.db.CartMapper;
import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.service.CartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@DubboService
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper mapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public OmsCartItem getUsersItem(String userId, String skuId) {
        OmsCartItem item = new OmsCartItem();
        item.setUserId(userId);
        item.setProductSkuId(skuId);
        return mapper.selectOne(item);
    }

    @Override
    public void addUsersItem(OmsCartItem cartItem) {
        mapper.insert(cartItem);
    }

    @Override
    public void updateUsersItem(OmsCartItem usersItem) {
        mapper.updateByPrimaryKeySelective(usersItem);
    }

    @Override
    public void flushCache(String userId) {
        //数据库查用户购物车
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setUserId(userId);
        List<OmsCartItem> usersCart = mapper.select(cartItem);
        //缓存层采用Redis的Hash结构，userID作key，skuID作field，cartItem作value
        //便于频繁地对同一用户的购物车条目进行增删改
        HashMap<String, String> cartMap = new HashMap<>();
        usersCart.forEach(item->{
            cartMap.put(item.getProductSkuId(), JSON.toJSONString(item));
        });
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        redisTemplate.delete("user:"+userId+":cart");
        hash.putAll("user:"+userId+":cart",cartMap);
    }

    @Override
    public List<OmsCartItem> getUsersCart(String userId) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        List<OmsCartItem> cartItems = new ArrayList<>();
        try {
            //Redis中通过hash key取出用户的所有购物车条目（每一条以JSON串保存）
            List<Object> values = hash.values("user:" + userId + ":cart");
            values.forEach(value->{
                //JSON拼回来
                OmsCartItem item = JSON.parseObject(value.toString(), OmsCartItem.class);
                //每一条目总价小计
                item.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                cartItems.add(item);
            });
        } catch (Exception e) {
            e.printStackTrace();
            //异常处理工业流程：1.获取错误信息，调用日志服务，保存信息
//            String errorMessage = e.getMessage();
//            logService.addErrlog(errorMessage);
            //2.返回null，防止调用层阻塞
            return null;
        }
        return cartItems;
    }

    @Override
    public void checkCart(String userId, Boolean isChecked, String productSkuId) {
        //修改数据库
        OmsCartItem item = new OmsCartItem();
        item.setIsChecked(isChecked);

        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("userId",userId)
                .andEqualTo("productSkuId",productSkuId);
        mapper.updateByExampleSelective(item,e);
        //同步缓存
        flushCache(userId);
    }
}
