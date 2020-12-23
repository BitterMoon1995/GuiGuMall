package com.lewo.zmail.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lewo.utils.Predicate;
import com.lewo.zmail.pay.function.AlipayFunction;
import com.lewo.zmail.pay.msg.MsgProvider;
import com.lewo.zmall.service.ErrorLogService;
import com.lewo.zmall.unified.iResult;
import com.lewo.utils.TimeUtils;
import com.lewo.zmail.pay.mapper.PaymentMapper;
import com.lewo.zmall.model.Payment;
import com.lewo.zmall.service.AlipayService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    MsgProvider msgProvider;
    @DubboReference
    ErrorLogService errLogService;
    @Autowired
    AlipayClient alipayClient;
    @Autowired
    AlipayFunction function;

    @Override
    public void newPayment(String orderSn, BigDecimal totalAmount, String subject) {
        /*发送延迟队列消息，以在之后检查交易支付状态*/
        iResult res = msgProvider.checkPayStatus(orderSn);
        if (Predicate.fail(res))
            errLogService.newError("MQ","订单提交发送延迟队列失败","orderSn",orderSn);

        Payment payment = new Payment();
        payment.setPaymentStatus("未支付");
        payment.setTotalAmount(totalAmount);
        payment.setSubject(subject);
        payment.setOrderSn(orderSn);
        payment.setCreateTime(TimeUtils.curTime());

        paymentMapper.insert(payment);
    }

    @Override
    public iResult sucPaid(Payment payment) {
        String orderSn = payment.getOrderSn();
        /*发送消息，通知订单系统*/
        iResult mqRes = msgProvider.afterSucPay(orderSn);
        if (Predicate.fail(mqRes))
            errLogService.newError("MQ","支付成功消息队列挂了","orderSn",orderSn);

        payment.setCallbackTime(TimeUtils.curTime());
        payment.setPaymentStatus("已支付");

        /*数据库更新交易单已支付状态*/
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

    @Override
    /*调用阿里爹api，查询订单支付状态*/
    public Map<String,Object> checkTradeStatus(String orderSn) {
        HashMap<String, Object> map = new HashMap<>();
        AlipayTradeQueryRequest queryRequest = function.genTradeQueryRequest(orderSn);

        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(queryRequest);
            System.out.println(response);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess())
            map.put("tradeStatus","QUERY_FAIL");
        else {
            map.put("tradeStatus",response.getTradeStatus());
            map.put("tradeNumber",response.getTradeNo());
        }
        return map;
    }

}
