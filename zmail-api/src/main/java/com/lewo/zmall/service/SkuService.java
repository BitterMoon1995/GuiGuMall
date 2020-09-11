package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsSkuInfo;
import com.lewo.zmall.model.PmsSkuSaleAttrValue;

import java.util.List;
import java.util.Map;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo skuInfo);

    PmsSkuInfo getById(String skuId) throws InterruptedException;

    void delSku(String id);

    List<PmsSkuInfo> getAllSkuBySpuId(String spuId);

    List<PmsSkuSaleAttrValue> getSaleAttrValue(String skuId);

    Map<String, String> generateSkuMap(String spuId);

    List<PmsSkuInfo> getAllSku();
}
