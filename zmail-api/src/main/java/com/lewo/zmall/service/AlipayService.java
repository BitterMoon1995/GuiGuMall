package com.lewo.zmall.service;

import com.lewo.zmall.unified.iResult;
import com.lewo.zmall.model.Payment;

import java.math.BigDecimal;
import java.util.Map;

public interface AlipayService {
    void newPayment(String outTradeNum, BigDecimal totalAmount, String subject);

    iResult updatePaidPayment(String aliTradeNum, String orderSn);

    Map<String,Object> checkTradeStatus(String orderSn);
}
