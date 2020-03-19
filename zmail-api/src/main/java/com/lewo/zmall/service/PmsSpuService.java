package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsBaseCatalog1;
import com.lewo.zmall.model.PmsBaseCatalog2;
import com.lewo.zmall.model.PmsBaseCatalog3;

import java.util.List;

/**
 * 一级分类表(PmsBaseCatalog1)表服务接口
 *
 * @author makejava
 * @since 2020-03-19 10:47:30
 */
public interface PmsSpuService {

    public List<PmsBaseCatalog1> getCatalog1();
    public List<PmsBaseCatalog2> getCatalog2(String id);
    public List<PmsBaseCatalog3> getCatalog3(String id);


    public List<PmsBaseAttrInfo> spuList(String id);
}