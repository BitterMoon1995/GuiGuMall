package com.lewo.zmail.order.db;

import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface OrderMapper extends Mapper<OmsOrder> {
}
