package com.lewo.zmail.pay.msg;

import com.lewo.zmall.service.AlipayService;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;

/**
 * 没有业务逻辑！学习类
 */
@Component
public class MsgConsumer {
    @Autowired
    AlipayService service;
    /*一个listener一个线程（SimpleAsyncTaskExecutor）*/
    @JmsListener(destination = "drink")
    public void queueReceiver(String msg,Session session){
        System.out.println("容器p2p消费者接收："+msg);
        try {
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
//    @JmsListener(destination = "drink")
//    public void queueReceiver2(String msg){
//        System.out.println("容器p2p消费者2接收："+msg);
//    }

    @JmsListener(destination = "speech",containerFactory = "plainFactory")
    public void topicReceiver(String msg){
        System.out.println("容器pub/sub消费者接收："+msg);
    }

    @JmsListener(destination = "speech",containerFactory = "durableFactory"
            , id = "diona")
    public void durableTopicReceiver(String msg){
        System.out.println("容器持久化pub/sub消费者接收："+msg);
    }
//    @JmsListener(destination = "speech",containerFactory = "durableFactory"
//            , id = "diona2",subscription = "nigger")
//    public void durableTopicReceiver2(String msg){
//        System.out.println("容器持久化pub/sub消费者接收："+msg);
//    }

//    @JmsListener(destination = "PAY_SUCCESS_QUEUE")
//    public void wanshua(Map<String,String> msgMap){
//        String orderSn = msgMap.get("orderSn");
//        System.out.println("当场抓获尼哥：\n"+orderSn);
//    }

    @JmsListener(destination = "miHoYo")
    public void queueConsume(ActiveMQMessage message, Session session) throws JMSException {
        System.out.println("额额");
        /*直接调session.rollback确实触发了死信，证明：
        * ★consumer默认是事务session我透！！！！！！！★
        * 写完process逻辑必须提交我操了！！！！！！！！
        * */

        session.rollback();
        System.out.println(message);
        if (message instanceof ActiveMQTextMessage) {
            ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        }
        else if (message instanceof ActiveMQMapMessage) {
            ActiveMQMapMessage mapMessage = (ActiveMQMapMessage) message;
            Map<String, Object> map = mapMessage.getContentMap();
            String nigger = map.get("nigger").toString();
            System.out.println(nigger);
        }
    }

    @JmsListener(destination = "PAYMENT_CHECK_QUEUE")
    public void consumeCheckPayStatus(Map<String, String> mapMessage,Session session){
        String orderSn = mapMessage.get("orderSn");
        String tradeStatus = service.checkTradeStatus(orderSn);
        System.out.println(tradeStatus);

        try {
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
