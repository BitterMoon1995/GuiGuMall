package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsBaseSaleAttr;
import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.model.PmsProductSaleAttr;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    String saveSpuInfo(PmsProductInfo pmsProductInfo);
}