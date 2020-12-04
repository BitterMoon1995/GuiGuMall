package com.lewo.zmall.service;

import com.lewo.unified.iResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface AlipayService {
    void newPayment(String outTradeNum, BigDecimal totalAmount, String subject);

    iResult paySuccess(HttpServletRequest request);
}
