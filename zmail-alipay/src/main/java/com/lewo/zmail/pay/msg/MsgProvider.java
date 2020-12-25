package com.lewo.zmail.pay.msg;

import com.lewo.zmall.unified.iResult;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 消息操作和持久层操作要解耦
 */
@Component
public class MsgProvider {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    /*用户付款后，向支付成功队列发送该订单已支付消息，由订单服务消费*/
    public iResult sendPaidMsg(String orderSn){
        HashMap<String, String> map = new HashMap<>();
        map.put("orderSn",orderSn);
        try {
            jmsTemplate.convertAndSend("PAYMENT_PAID_QUEUE",map);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
            return iResult.mqException;
        }
        return iResult.success;
    }

    /*发送消息到延迟队列，检查用户支付状态*/
    public iResult sendCheckPayStatusMsg(String orderSn, Integer count){
        /*checkMsg在service-provider-consumer间循环的阀门*/
        if (count <= 0)
            return iResult.success;

        HashMap<String, Object> headers = new HashMap<>();
        headers.put(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*5);
        HashMap<String, Object> mapMessage = new HashMap<>();
        mapMessage.put("orderSn",orderSn);
        mapMessage.put("count",count);

        try {
            jmsTemplate.convertAndSend("PAYMENT_CHECK_QUEUE",mapMessage,headers);
        } catch (MessagingException e) {
            return iResult.mqException;
        }
        return iResult.success;
    }
}
