package com.lewo.zmall.service;

import com.lewo.zmall.model.*;

import java.util.List;

/**
 * 一级分类表(PmsBaseCatalog1)表服务接口
 *
 * @author makejava
 * @since 2020-03-19 10:47:30
 */
public interface PmsBaseService {

    public List<PmsBaseCatalog1> getCatalog1();
    public List<PmsBaseCatalog2> getCatalog2(String id);
    public List<PmsBaseCatalog3> getCatalog3(String id);


    public List<PmsBaseAttrInfo> getAttrList(String id);
    public String saveAttrInfo(PmsBaseAttrInfo attrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String infoId);
}