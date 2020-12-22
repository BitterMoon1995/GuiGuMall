package com.lewo.zmail.pay.service;

import com.lewo.zmall.unified.iResult;
import com.lewo.utils.TimeUtils;
import com.lewo.zmail.pay.mapper.PaymentMapper;
import com.lewo.zmall.model.Payment;
import com.lewo.zmall.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Override
    public void genPayment(String outTradeNum, BigDecimal totalAmount, String subject) {
        Payment payment = new Payment();
        payment.setPaymentStatus("未支付");
        payment.setTotalAmount(totalAmount);
        payment.setSubject(subject);
        payment.setOrderSn(outTradeNum);
        payment.setCreateTime(TimeUtils.curTime());

        paymentMapper.insert(payment);
    }

    @Override
    public iResult sucPaid(Payment payment) {
        payment.setCallbackTime(TimeUtils.curTime());
        payment.setPaymentStatus("已支付");
        String orderSn = payment.getOrderSn();

        /*数据库更新支付单已支付状态*/
        Example e = new Example(Payment.class);
        e.createCriteria().andEqualTo("orderSn", orderSn);
        try {
            paymentMapper.updateByExampleSelective(payment,e);
        } catch (Exception exception) {
            exception.printStackTrace();
            return iResult.dbException;
        }

        return iResult.success;
    }
}
