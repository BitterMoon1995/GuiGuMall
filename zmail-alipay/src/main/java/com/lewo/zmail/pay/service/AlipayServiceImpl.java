package com.lewo.zmail.pay.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.lewo.unified.iResult;
import com.lewo.utils.TimeUtils;
import com.lewo.zmail.pay.mapper.PaymentMapper;
import com.lewo.zmall.model.Payment;
import com.lewo.zmall.service.AlipayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    PaymentMapper paymentMapper;

    @Override
    public void newPayment(String outTradeNum, BigDecimal totalAmount, String subject) {
        Payment payment = new Payment();
        payment.setPaymentStatus("未支付");
        payment.setTotalAmount(totalAmount);
        payment.setSubject(subject);
        payment.setOrderSn(outTradeNum);
        payment.setCreateTime(TimeUtils.curTime());

        paymentMapper.insert(payment);
    }

    @Override
    public iResult paySuccess(HttpServletRequest request) {
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


        String callbackContent = request.getQueryString();//Returns the query string that is contained in the request URL after the path.
        System.out.println(out_trade_no+total_amount+trade_no);

        //验签，确认是阿里的回调，再走DB！
        if (StringUtils.isBlank(sign))
//            AlipaySignature.certVerifyV1(......)
            return iResult.illegalParam;

        Payment payment = new Payment();
        payment.setCallbackTime(TimeUtils.curTime());
        payment.setPaymentStatus("已支付");
        payment.setAlipayTradeNo(trade_no);
        payment.setCallbackContent(callbackContent);

        Example e = new Example(Payment.class);
        e.createCriteria().andEqualTo("orderSn", out_trade_no);
        try {
            paymentMapper.updateByExampleSelective(payment,e);
        } catch (Exception exception) {
            exception.printStackTrace();
            return iResult.dbException;
        }
        return iResult.success;
    }
}
