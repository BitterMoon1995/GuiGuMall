package com.lewo.zmall.service;

import com.lewo.zmall.unified.iResult;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;

import java.util.List;

public interface OrderService {

    String genTradeCode(String userId);

    iResult checkTradeCode(String userId, String tradeCode);

    void insertOrder(OmsOrder order, List<OmsOrderItem> orderItems);

    iResult orderSucPayStatusUpdate(String orderSn);
}
