package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.*;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Autowired
    ProductSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    PmsBaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper productSaleAttrValueMapper;
    @Autowired
    PmsProductImageMapper imageMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo productInfo = new PmsProductInfo();
        productInfo.setCatalog3Id(catalog3Id);
        return productInfoMapper.select(productInfo);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        productInfoMapper.insertSelective(pmsProductInfo);
        String productId = pmsProductInfo.getId();

        //商品销售属性绑ID
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr saleAttr : spuSaleAttrList) {
            saleAttr.setProductId(productId);
            spuSaleAttrMapper.insertSelective(saleAttr);
            //属性值绑ID
            List<PmsProductSaleAttrValue> saleAttrValueList = saleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue saleAttrValue : saleAttrValueList) {
                saleAttrValue.setProductId(productId);
                productSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
        //图片绑ID
        List<PmsProductImage> imageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage productImage : imageList) {
            productImage.setProductId(productId);
            imageMapper.insertSelective(productImage);
        }
        return "周神！";
    }

    @Override
    public void delSpuInfo(String id) {
        PmsProductSaleAttr saleAttr = new PmsProductSaleAttr();
        saleAttr.setProductId(id);
        spuSaleAttrMapper.delete(saleAttr);

        PmsProductSaleAttrValue saleAttrValue = new PmsProductSaleAttrValue();
        saleAttrValue.setProductId(id);
        productSaleAttrValueMapper.delete(saleAttrValue);

        PmsProductImage productImage = new PmsProductImage();
        productImage.setProductId(id);
        imageMapper.delete(productImage);

        productInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr productSaleAttr = new PmsProductSaleAttr();
        productSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> saleAttrs = spuSaleAttrMapper.select(productSaleAttr);
        //商品销售属性注入对应的属性值
        saleAttrs.forEach(attr->{
            PmsProductSaleAttrValue attrValue = new PmsProductSaleAttrValue();
            attrValue.setProductId(spuId);
            /*小卡：属性值的sale_attr_id字段对应的【并不是】属性的主键id字段
            而是属性的sale_attr_id字段。永远记住商品的销售属性种类是由平台决定的，是固定的（颜色.尺寸.版本.容量）
            每个SPU所持有的销售属性的id字段仅仅是个自增主键，关键由sale_attr_id字段来决定到底是哪个销售属性
            */
            attrValue.setSaleAttrId(attr.getSaleAttrId());

            List<PmsProductSaleAttrValue> values = productSaleAttrValueMapper.select(attrValue);
            System.out.println(values);

            attr.setSpuSaleAttrValueList(values);
        });
        return saleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage productImage = new PmsProductImage();
        productImage.setProductId(spuId);
        return imageMapper.select(productImage);
    }

}
