package com.lewo.zmail.order.msg;

import com.alibaba.fastjson.JSON;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.unified.iResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MsgProvider {
    @Autowired
    JmsMessagingTemplate jmsTemplate;
    /*
    在订单更新已支付状态后，通知库存系统。
    以JSON串的形式传递整个订单信息
     */
    public iResult orderPaidPost(OmsOrder order){
        System.out.println(order);
        String orderStr = JSON.toJSONString(order);

        try {
            jmsTemplate.convertAndSend("ORDER_PAID_QUEUE",orderStr);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("public iResult orderPaidPost(OmsOrder order)");
            return iResult.mqException;
        }
        return iResult.success;
    }
}
