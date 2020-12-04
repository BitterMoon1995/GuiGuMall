package com.lewo.zmail.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.lewo.unified.iResult;
import com.lewo.utils.Predicate;
import com.lewo.utils.RandomUtils;
import com.lewo.zmail.pay.function.AlipayFunction;
import com.lewo.zmail.web.annotation.Entrance;
import com.lewo.zmail.web.filter.CheckLogin;
import com.lewo.zmall.service.AlipayService;
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

    @Entrance
    @CheckLogin
    @RequestMapping("index.html")
    public String index(String outTradeNum, BigDecimal totalAmount,
                        HttpServletRequest request, ModelMap modelMap){
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("outTradeNum",outTradeNum);
        return "index";
    }

    @RequestMapping("alipay/submit")
    @CheckLogin
    @ResponseBody
    public String submit(String outTradeNum, BigDecimal totalAmount){
        String form;
        String subject = "可莉小乖宝";
        outTradeNum = RandomUtils.genNonceStr();
        try {
            form = alipayClient.pageExecute(function.genPagePayRequest(outTradeNum,totalAmount.longValue(),subject)).getBody();
            System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        service.newPayment(outTradeNum,totalAmount,subject);
        return form;
    }

    @RequestMapping("alipay/callback")
    public String sucPay(HttpServletRequest request, HttpServletResponse response) {
        iResult res = service.paySuccess(request);
        if (!Predicate.suc(res))
            System.out.println("麻了，给钱没办了事");
        //再通知支付宝一次，形式为返回"success"字符串（？）
        try {
            response.getWriter().println("success");//看不懂（流汗黄豆）
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
