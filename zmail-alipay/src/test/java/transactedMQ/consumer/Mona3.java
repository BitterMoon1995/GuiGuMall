package transactedMQ.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 非事务、自动告知
 */
public class Mona3 {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("genshin");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println(text);
                /*
                自动告知模式，不用调用ack()手动告知
                 */
//                message.acknowledge();

                /*
                session调recover，还是触发死信机制
                 */
//                session.recover();

                session.commit();
                /*
                ★(此MessageListener的onMessage())方法不能成功返回，同样触发死信。★
                 */
//                String s = null;
//                System.out.println(s.length());

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
