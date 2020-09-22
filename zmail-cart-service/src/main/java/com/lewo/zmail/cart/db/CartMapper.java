package com.lewo.zmail.cart.db;

import com.lewo.zmall.model.OmsCartItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CartMapper extends Mapper<OmsCartItem> {
}
