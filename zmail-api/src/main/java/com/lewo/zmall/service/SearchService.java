package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsSearchParam;
import com.lewo.zmall.model.PmsSearchSkuInfo;
import com.lewo.zmall.model.PmsSkuAttrValue;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    public List<PmsSearchSkuInfo> searchSku(PmsSearchParam param,
    Integer from, Integer size,String sortField,Boolean isDesc) throws IOException;
}
