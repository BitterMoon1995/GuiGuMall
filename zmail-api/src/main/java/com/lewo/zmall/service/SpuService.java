package com.lewo.zmall.service;

import com.lewo.zmall.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    void delSpuInfo(String id);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> getSaleAttr(String spuId,String skuId);

    List<PmsProductSaleAttr> getCheckedSaleAttr(String spuId,String skuId);
}
