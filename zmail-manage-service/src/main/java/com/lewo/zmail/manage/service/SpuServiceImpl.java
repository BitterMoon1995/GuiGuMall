package com.lewo.zmail.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.ProductInfoMapper;
import com.lewo.zmail.manage.dao.ProductSaleAttrMapper;
import com.lewo.zmall.model.PmsBaseSaleAttr;
import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.model.PmsProductSaleAttr;
import com.lewo.zmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Autowired
    ProductSaleAttrMapper saleAttrMapper;
    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo productInfo = new PmsProductInfo();
        productInfo.setCatalog3Id(catalog3Id);
        return productInfoMapper.select(productInfo);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return saleAttrMapper.selectAll();
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        productInfoMapper.insertSelective(pmsProductInfo);
        return "周神！";
    }

}
