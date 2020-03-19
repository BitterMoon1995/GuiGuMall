package com.lewo.zmail.manage.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.manage.dao.PmsAttrInfoMapper;
import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsBaseCatalog1;
import com.lewo.zmail.manage.dao.PmsBaseCatalog1Dao;
import com.lewo.zmail.manage.dao.PmsBaseCatalog2Dao;
import com.lewo.zmail.manage.dao.PmsBaseCatalog3Dao;
import com.lewo.zmall.model.PmsBaseCatalog2;
import com.lewo.zmall.model.PmsBaseCatalog3;
import com.lewo.zmall.service.PmsSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 一级分类表(PmsBaseCatalog1)表服务实现类
 *
 * @author makejava
 * @since 2020-03-19 10:47:30
 */
@Service
public class PmsSpuServiceImpl implements PmsSpuService {
    @Autowired
    PmsBaseCatalog1Dao pmsBaseCatalog1Dao;
    @Autowired
    PmsBaseCatalog2Dao pmsBaseCatalog2Dao;
    @Autowired
    PmsBaseCatalog3Dao pmsBaseCatalog3Dao;
    @Autowired
    PmsAttrInfoMapper attrInfoMapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Dao.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String id) {
        Example example = new Example(PmsBaseCatalog2.class);
        example.createCriteria().andEqualTo("catalog1Id",id);
        return pmsBaseCatalog2Dao.selectByExample(example);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(id);
        return pmsBaseCatalog3Dao.select(pmsBaseCatalog3);
    }

    @Override
    public List<PmsBaseAttrInfo> spuList(String id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(id);
        return attrInfoMapper.select(pmsBaseAttrInfo);
    }

}