package com.lewo.zmail.search.service;

import com.lewo.zmail.search.dao.PmsAttrInfoMapper;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsBaseAttrValue;
import com.lewo.zmall.service.PlatformAttrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    public List<PmsBaseAttrInfo> delSelectedAttr(List<PmsBaseAttrInfo> aggAttrs, String[] toDelValueIds) {
        Iterator<PmsBaseAttrInfo> iterator = aggAttrs.iterator();
        //遍历待删除的属性值ID集合
        for (String delValueId : toDelValueIds) {
            //集合的删除一定要用迭代器
            while (iterator.hasNext()) {
                PmsBaseAttrInfo attr = iterator.next();
                List<PmsBaseAttrValue> valueList = attr.getAttrValueList();
                //遍历当前属性的属性值集合
                for (PmsBaseAttrValue value : valueList) {
                    //获取当前属性值的ID
                    String valueId = value.getId();
                    {
                        //如果当前属性值ID等于待删除属性值ID集合的任意一个
                        if (valueId.equals(delValueId))
                            //则删除当前属性
                            iterator.remove();
                    }
                }
            }
        }
        return aggAttrs;
    }
}
