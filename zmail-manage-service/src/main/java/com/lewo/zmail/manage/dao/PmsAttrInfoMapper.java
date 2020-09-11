package com.lewo.zmail.manage.dao;

import com.lewo.zmall.model.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {

    List<PmsBaseAttrInfo> attrListByValues(@Param("valueIdStr") String valueIdStr);
}
