package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.PmsBaseSaleAttrMapper;
import com.lewo.zmail.manage.dao.PmsProductSaleAttrValueMapper;
import com.lewo.zmail.manage.dao.ProductInfoMapper;
import com.lewo.zmail.manage.dao.ProductSaleAttrMapper;
import com.lewo.zmall.model.PmsBaseSaleAttr;
import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.model.PmsProductSaleAttr;
import com.lewo.zmall.model.PmsProductSaleAttrValue;
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
        return "周神！";
    }

}
