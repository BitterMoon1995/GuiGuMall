package com.lewo.zmail.manage.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.*;
import com.lewo.zmall.model.*;
import com.lewo.zmall.service.PmsBaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 一级分类表(PmsBaseCatalog1)表服务实现类
 *
 * @author makejava
 * @since 2020-03-19 10:47:30
 */
@DubboService
public class PmsBaseServiceImpl implements PmsBaseService {
    @Autowired
    PmsBaseCatalog1Dao pmsBaseCatalog1Dao;
    @Autowired
    PmsBaseCatalog2Dao pmsBaseCatalog2Dao;
    @Autowired
    PmsBaseCatalog3Dao pmsBaseCatalog3Dao;
    @Autowired
    PmsAttrInfoMapper attrInfoMapper;
    @Autowired
    PmsAttrValueMapper attrValueMapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Dao.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String id) {
        Example example = new Example(PmsBaseCatalog2.class);
        example.createCriteria().andEqualTo("catalog1Id", id);
        return pmsBaseCatalog2Dao.selectByExample(example);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(id);
        return pmsBaseCatalog3Dao.select(pmsBaseCatalog3);
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrList(String id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(id);
        List<PmsBaseAttrInfo> attrInfos = attrInfoMapper.select(pmsBaseAttrInfo);
        attrInfos.forEach(info -> {
            PmsBaseAttrValue value = new PmsBaseAttrValue();
            value.setAttrId(info.getId());
            List<PmsBaseAttrValue> values = attrValueMapper.select(value);
            info.setAttrValueList(values);
        });
        return attrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo attrInfo) {
        boolean blank = StringUtils.isBlank(attrInfo.getId());
        if (blank) {
            //插入属性名
            attrInfoMapper.insertSelective(attrInfo);
            //批量插入属性值
            List<PmsBaseAttrValue> attrValueList = attrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        } else {
            //先改属性名
            attrInfoMapper.updateByPrimaryKeySelective(attrInfo);
            //再删该属性名对应的旧属性值
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(attrInfo.getId());
            attrValueMapper.delete(pmsBaseAttrValue);
            //遍历传过来的属性值集合
            List<PmsBaseAttrValue> attrValueList = attrInfo.getAttrValueList();
            for (PmsBaseAttrValue baseAttrValue : attrValueList) {
                //★★★将属性值和当前属性名绑定，前端没绑！
                baseAttrValue.setAttrId(attrInfo.getId());
                //再插入到数据库
                attrValueMapper.insertSelective(baseAttrValue);
            }
        }
        return "success";

    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String infoId) {
        PmsBaseAttrValue attrValue = new PmsBaseAttrValue();
        attrValue.setAttrId(infoId);
        return attrValueMapper.select(attrValue);
    }

}