package com.lewo.zmall.service;

import com.lewo.unified.iResult;

public interface OrderService {

    String genTradeCode(String userId);

    iResult checkTradeCode(String userId, String tradeCode);
}
