package com.lewo.zmail.pay.function;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.lewo.utils.RandomUtils;
import com.lewo.zmail.pay.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
@Component
public class AlipayFunction {
    @Autowired
    AlipayConfig payConfig;

    public AlipayTradePagePayRequest genPagePayRequest(String outTradeNum, Long tta,String subject){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(payConfig.return_payment_url);
        request.setNotifyUrl(payConfig.notify_payment_url);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("out_trade_no", outTradeNum);
        paramMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        paramMap.put("total_amount",tta);
        paramMap.put("subject",subject);

        String paramStr = JSON.toJSONString(paramMap);
        request.setBizContent(paramStr);
        return request;
    }
}
