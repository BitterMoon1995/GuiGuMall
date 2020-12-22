package com.lewo.zmail.order.msg;

import com.alibaba.fastjson.JSON;
import com.lewo.zmall.model.OmsOrder;
import com.lewo.zmall.unified.iResult;
import com.lewo.utils.Predicate;
import com.lewo.zmall.service.ErrorLogService;
import com.lewo.zmall.service.OrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;
@Slf4j
@Component
public class MsgConsumer {

    @Autowired
    OrderService service;
    @Autowired
    JmsMessagingTemplate jmsTemplate;
    @DubboReference
    ErrorLogService errLogService;

    /*
    方法参数要......优雅一点！
     */
    @JmsListener(destination = "PAYMENT_PAID_QUEUE")
    public void receiveSucPay(Map<String, Object> map, Session session){

        /*取消息*/
        String orderSn = map.get("orderSn").toString();
        System.out.println("Message Check：\n"+orderSn);

        /*更新订单已支付状态*/
        iResult res = service.orderSucPayStatusUpdate(orderSn);
        if (Predicate.suc(res)){
            System.out.println("订单成功更新已支付状态！这可莉害了");
        }
        else {
            errLogService.newError("order_db"
                    ,"订单更新已支付状态失败","orderSn",orderSn);
        }
        try {
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    @JmsListener(destination = "ORDER_PAID_QUEUE")
    public void testInventoryReceive(String orderStr, Session session){
        OmsOrder order = JSON.parseObject(orderStr, OmsOrder.class);
        System.out.println(order);
        try {
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
