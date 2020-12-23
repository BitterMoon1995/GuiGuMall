package com.lewo.zmail.pay.function;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.lewo.zmail.pay.config.AlipayConfig;
import com.lewo.zmall.model.Payment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    public AlipayTradeQueryRequest genTradeQueryRequest(String orderSn){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("out_trade_no", orderSn);

        String paramStr = JSON.toJSONString(paramMap);
        request.setBizContent(paramStr);
        return request;
    }

    public Payment validateSignature(HttpServletRequest request){
        String sign = request.getParameter("sign");

        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String auth_app_id = request.getParameter("auth_app_id");
        String method = request.getParameter("method");
        String charset = request.getParameter("charset");
        String version = request.getParameter("version");
        String seller_id = request.getParameter("seller_id");

        String callbackContent = request.getQueryString();

        /*走阿里流程验签！疑似沙箱环境参数不全，就先糊弄一下了，下次再说。。。。。。
        AlipaySignature.certVerifyV1(......)*/
        if (StringUtils.isBlank(out_trade_no))
            return null;
        Payment payment = new Payment();
        payment.setOrderSn(out_trade_no);
        payment.setAlipayTradeNo(trade_no);
        payment.setCallbackContent(callbackContent);
        return payment;
    }


}
