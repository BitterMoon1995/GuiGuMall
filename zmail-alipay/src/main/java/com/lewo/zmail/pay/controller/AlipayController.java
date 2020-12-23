package com.lewo.zmail.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.lewo.zmail.pay.msg.MsgProvider;
import com.lewo.zmall.service.ErrorLogService;
import com.lewo.zmall.unified.iResult;
import com.lewo.utils.Predicate;
import com.lewo.utils.RandomUtils;
import com.lewo.zmail.pay.function.AlipayFunction;
import com.lewo.zmail.web.annotation.Entrance;
import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmall.model.Payment;
import com.lewo.zmall.service.AlipayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@Controller
@CrossOrigin
public class AlipayController {
    @Autowired
    AlipayClient alipayClient;
    @Autowired
    AlipayFunction function;
    @Autowired
    AlipayService service;
    @DubboReference
    ErrorLogService errLogService;

    @Entrance
    @CheckLogin
    @RequestMapping("index.html")
    /*订单金额不能为0，不然马云报‘错误码：TOTAL_FEE_EXCEED’*/
    public String index(String orderSn, BigDecimal totalAmount,
                        HttpServletRequest request, ModelMap modelMap){
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("outTradeNum",orderSn);
        return "index";
    }

    @RequestMapping("alipay/submit")
    @CheckLogin
    @ResponseBody
    public String submit(String outTradeNum, BigDecimal totalAmount){
        String form;
        String subject = "可莉小乖宝";
        if (StringUtils.isBlank(outTradeNum))
            //测试代码
            outTradeNum = RandomUtils.genNonceStr();
        try {
            form = alipayClient.pageExecute(function.genPagePayRequest(outTradeNum,totalAmount.longValue(),subject)).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        service.newPayment(outTradeNum,totalAmount,subject);

        return form;
    }

    @RequestMapping("alipay/callback")
    public String sucPay(HttpServletRequest request, HttpServletResponse response) {

        Payment payment = function.validateSignature(request);
        if (payment == null)//验签失败
            return "error";

        /*调Service，发送sucPay消息、修改支付单状态*/
        iResult res = service.sucPaid(payment);
        if (Predicate.fail(res))
            errLogService.newError("DB","支付成功后尝试改支付单状态，持久层挂了","orderSn",payment.getOrderSn());
        /*再通知支付宝一次，形式为返回"success"字符串（？）*/
        try {
            response.getWriter().println("success");//看不懂（流汗黄豆）
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
