package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.PmsSkuAttrValueMapper;
import com.lewo.zmail.manage.dao.PmsSkuImageMapper;
import com.lewo.zmail.manage.dao.PmsSkuInfoMapper;
import com.lewo.zmail.manage.dao.PmsSkuSaleAttrValueMapper;
import com.lewo.zmall.model.PmsSkuAttrValue;
import com.lewo.zmall.model.PmsSkuImage;
import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.model.PmsSkuSaleAttrValue;
import com.lewo.zmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuAttrValueMapper baseAttrValueMapper;
    @Autowired
    PmsSkuInfoMapper skuInfoMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper saleAttrValueMapper;
    @Autowired
    PmsSkuImageMapper skuImageMapper;

    @Override
    public void saveSkuInfo(PmsSkuInfo skuInfo) {
        //基本信息
        skuInfoMapper.insert(skuInfo);
        //得到存入后的ID
        List<PmsSkuInfo> vars = skuInfoMapper.select(skuInfo);
        if (vars.size()==0) return;
        PmsSkuInfo var = vars.get(0);
        System.out.println(var);
        String skuId = var.getId();

        //base平台属性
        List<PmsSkuAttrValue> baseAttrValueList = skuInfo.getSkuAttrValueList();
        baseAttrValueList.forEach(baseAttr->{
            baseAttr.setSkuId(skuId);
            baseAttrValueMapper.insert(baseAttr);
        });
        //sku销售属性
        List<PmsSkuSaleAttrValue> saleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        saleAttrValueList.forEach(saleAttr->{
            saleAttr.setSkuId(skuId);
            saleAttrValueMapper.insert(saleAttr);
        });
        //图片
        List<PmsSkuImage> imageList = skuInfo.getSkuImageList();
        imageList.forEach(img->{
            img.setSkuId(skuId);
            skuImageMapper.insert(img);
        });
    }
}
