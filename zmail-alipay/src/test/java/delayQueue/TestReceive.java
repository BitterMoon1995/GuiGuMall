package delayQueue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TestReceive {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

        Queue queue = session.createQueue("PAYMENT_CHECK_QUEUE");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                String text = ((MapMessage) message).getString("orderSn");
                System.out.println(text);
                session.commit();

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
