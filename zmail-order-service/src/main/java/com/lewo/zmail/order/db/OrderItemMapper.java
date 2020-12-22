package com.lewo.zmail.order.db;

import com.lewo.zmall.model.OmsOrderItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface OrderItemMapper extends Mapper<OmsOrderItem> {
}
