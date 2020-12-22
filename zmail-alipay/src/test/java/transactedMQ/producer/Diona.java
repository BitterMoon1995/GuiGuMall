package transactedMQ.producer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.util.HashMap;

/**
 * 理一下事务消息发送
 */
public class Diona {
    @Test
    public void transacted() throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Queue queue = session.createQueue("genshin");
        MessageProducer producer = session.createProducer(queue);

        TextMessage message = new ActiveMQTextMessage();
        message.setText("smlmz");
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(message);

        /*
        提交了才能正常发送，消息入队，未接收则pending。
        不提交就不入队，该消息不会发送，不会存在于任何地方
         */
        session.commit();
        /*
        不提交并回滚也是一样的效果。
        但是提交了又回滚是无效的，消息将正常发送出去
         */
        session.rollback();
        connection.close();

    }
    @Test
    public void manual_acknowledge() throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("genshin");
        MessageProducer producer = session.createProducer(queue);

        TextMessage message = new ActiveMQTextMessage();
        message.setText("smlmz");
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        /*疑似不能对消息的发送造成任何影响*/
//        session.recover();

        producer.send(message);
//        session.recover();

        connection.close();
    }
    @Test
    public void client_autoAcknowledge() throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("genshin");
        MessageProducer producer = session.createProducer(queue);

        TextMessage message = new ActiveMQTextMessage();
        message.setText("smlmz");
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(message);
        /*会报错，当然这个发送消息的方法肯定就没有正常返回。但是消息依然发送出去了*/
//        String s = null;
//        System.out.println(s.length());

        connection.close();
    }
    @Test
    public void testSpringConsumer() throws JMSException{
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("miHoYo");
        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("slmz");

        //HashMap<String, String> map = new HashMap<>();
        //map.put("nigger","尼哥儿尼哥儿小尼哥儿");
        //producer.send(map);
        /*
        标准api发送map只能发ActiveMQMapMessage，
        不像spring爸爸提供的JmsTemplate可以让你直接发HashMap（他会帮忙封装？），
        而@JmsListener注解的消费方listener，这两种发送方式都可以正常接收到消息。

        发null消息：
        标准api报NPE，发不出去，不入队，不pending，也不入死信；
        JmsTemplate报java.lang.IllegalArgumentException: Payload must not be null，
        发送结果同上。
         */
        ActiveMQMapMessage mapMessage = new ActiveMQMapMessage();
        mapMessage.setString("nigger","麻了");
//        producer.send(null);//NPE
        producer.send(mapMessage);


        connection.close();
    }
}
