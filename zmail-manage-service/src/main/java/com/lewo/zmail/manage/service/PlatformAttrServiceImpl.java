package com.lewo.zmail.manage.service;

import com.lewo.zmail.manage.dao.PmsAttrInfoMapper;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.service.PlatformAttrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@DubboService
public class PlatformAttrServiceImpl implements PlatformAttrService {
    @Autowired
    PmsAttrInfoMapper attrMapper;
    @Override
    public List<PmsBaseAttrInfo> attrListByValues(Set<String> baseAttrSet) {
        //把集合拼成串，按逗号分隔，用于拼接SQL
        String valueIdStr = StringUtils.join(baseAttrSet, ",");
        return attrMapper.attrListByValues(valueIdStr);
    }
}
