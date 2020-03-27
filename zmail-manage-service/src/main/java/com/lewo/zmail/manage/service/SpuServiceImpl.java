package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.*;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Autowired
    ProductSaleAttrMapper saleAttrMapper;
    @Autowired
    PmsBaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper attrValueMapper;
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

        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr saleAttr : spuSaleAttrList) {
            saleAttr.setProductId(productId);
            saleAttrMapper.insertSelective(saleAttr);

            List<PmsProductSaleAttrValue> saleAttrValueList = saleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue saleAttrValue : saleAttrValueList) {
                saleAttrValue.setProductId(productId);
                attrValueMapper.insertSelective(saleAttrValue);
            }
        }

        List<PmsProductImage> imageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage productImage : imageList) {
            productImage.setProductId(productId);
            imageMapper.insertSelective(productImage);
        }
        return "周神！";
    }

}
