package com.lewo.zmail.order.service;

import com.lewo.unified.iResult;
import com.lewo.utils.CommonUtils;
import com.lewo.zmall.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    RedisTemplate<String, Object> redis;

    @Override
    public String genTradeCode(String userId) {
        String tradeCode = CommonUtils.genNonceStr();
        redis.opsForValue().set("userId:"+userId+":tradeCode",tradeCode,30, TimeUnit.MINUTES);
        return tradeCode;
    }

    @Override
    public iResult checkTradeCode(String userId, String tradeCode) {
        String trueCode = Objects.requireNonNull(redis.opsForValue().get("userId:" + userId + ":tradeCode")).toString();
        if (StringUtils.isBlank(trueCode))
            return iResult.emptyParam;
        if (!trueCode.equals(tradeCode))
            return iResult.illegalParam;
        redis.delete("userId:" + userId + ":tradeCode");
        return iResult.success;
    }
}
