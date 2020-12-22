package transactedMQ.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 理一下事务消息接收
 */
public class Mona {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("genshin");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println(text);
                /*
                不提交就收不到消息，消息继续pending
                 */
//                session.commit();
                /*
                直接rollback竟然触发了死信机制：
                提供者连发7次消息（全部被消费者rollback），
                然后控制台显示一条消息正常进队出队，正常消费，
                但是死信队列新增一条pending消息。

                commit了又rollback，还是和提供者一样，消息被正常消费。
                 */
                session.rollback();

                /*
                只调ack无效，消息继续pending
                 */
//                message.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
                try {
                    session.rollback();
                } catch (JMSException jmsException) {
                    jmsException.printStackTrace();
                }
            }
        });
    }
}
