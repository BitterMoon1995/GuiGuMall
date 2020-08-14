package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsSkuInfo;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo skuInfo);

    PmsSkuInfo getById(String skuId);

    void delSku(String id);
}
