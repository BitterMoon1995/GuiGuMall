package com.lewo.zmail.manage.dao;

import com.lewo.zmall.model.PmsProductInfo;
import com.lewo.zmall.model.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductInfoMapper extends Mapper<PmsProductInfo> {

    List<PmsProductSaleAttr> getCheckedSaleAttr(@Param("spuId") String spuId,@Param("skuId") String skuId);
}
