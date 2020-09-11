package com.lewo.zmall.service;

import com.lewo.zmall.model.PmsBaseAttrInfo;

import java.util.List;
import java.util.Set;

public interface PlatformAttrService {
    //根据平台属性值查出平台属性
    List<PmsBaseAttrInfo> attrListByValues(Set<String> baseAttrSet);
}
