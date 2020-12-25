package com.lewo.zmail.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.easysdk.factory.Factory;
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
    /*创建新交易单，初始化平台订单号、总价、商品名称*/
    public void newPayment(String orderSn, BigDecimal totalAmount, String subject) {
        /*初始化延迟队列计数器。
        发送checkMsg达到此次数，客户还未付款，则不再发送，并......*/
        Integer count = 5;
        /*发送延迟队列消息，以在之后周期性检查交易支付状态*/
        iResult res = msgProvider.sendCheckPayStatusMsg(orderSn,count);
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
    /*更新交易单已支付状态*/
    public iResult updatePaidPayment(String aliTradeNum, String orderSn) {
        /*幂等性检查*/
        Payment query = new Payment();
        query.setOrderSn(orderSn);
        String paymentStatus = paymentMapper.selectOne(query).getPaymentStatus();
        if (paymentStatus.equals("已支付"))
            return iResult.success;

        /*发送消息，通知订单系统*/
        iResult mqRes = msgProvider.sendPaidMsg(orderSn);
        if (Predicate.fail(mqRes))
            errLogService.newError("MQ","支付成功消息队列挂了","orderSn",orderSn);

        Payment payment = new Payment();
        payment.setAlipayTradeNo(aliTradeNum);
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
    /*调用阿里爹api，根据平台订单序列号查询订单支付状态*/
    public Map<String,Object> checkTradeStatus(String orderSn) {
        HashMap<String, Object> map = new HashMap<>();
        /*封装请求，调用client*/
        AlipayTradeQueryRequest queryRequest = function.genTradeQueryRequest(orderSn);

        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(queryRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        /*三种状态：
        * 用户未扫码交易未创建；
        * 用户扫码但未付款，WAIT_BUYER_PAY
        * 用户扫码付款，TRADE_SUCCESS*/
        if (response == null || !response.isSuccess())
            map.put("tradeStatus","QUERY_FAIL");
        else {
            /*if isSuccess==false ,tradeStatus==null*/
            map.put("tradeStatus",response.getTradeStatus());
            map.put("tradeNo",response.getTradeNo());
        }
        return map;
    }

}
