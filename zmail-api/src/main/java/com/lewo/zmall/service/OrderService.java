package com.lewo.zmall.service;

import com.lewo.unified.iResult;
import com.lewo.zmall.model.OmsCartItem;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.model.OmsOrderItem;

import java.util.List;

public interface OrderService {

    String genTradeCode(String userId);

    iResult checkTradeCode(String userId, String tradeCode);

    void insertOrder(OmsOrder order, List<OmsOrderItem> orderItems);
}
