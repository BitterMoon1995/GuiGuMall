package com.lewo.zmall.service;

import com.lewo.zmall.unified.iResult;
import com.lewo.zmall.model.Payment;

import java.math.BigDecimal;

public interface AlipayService {
    void genPayment(String outTradeNum, BigDecimal totalAmount, String subject);

    iResult sucPaid(Payment payment);

}
