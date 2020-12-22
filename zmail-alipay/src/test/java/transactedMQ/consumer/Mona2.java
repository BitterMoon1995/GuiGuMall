package transactedMQ.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 非事务、手动告知
 */
public class Mona2 {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("genshin");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println(text);


                session.commit();
                /*
                手动告知模式：
                不提交、不ack，消息pending；
                提交，不ack，抛异常：javax.jms.IllegalStateException: Not a transacted session，
                且消息继续pending。
                不提交，ack，消息正常接收，pending消息出队
                 */
//                message.acknowledge();
                /*
                先调recover=只调recover，触发死信机制
                 */
                //session.recover();
            } catch (JMSException e) {
                e.printStackTrace();
                try {
                    session.recover();
                } catch (JMSException jmsException) {
                    jmsException.printStackTrace();
                }
            }
        });
    }
}
