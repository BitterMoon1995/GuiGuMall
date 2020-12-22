package msgConsumers;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class FeiLong {
    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.156.128:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination testSpeech = session.createTopic("speech");
            Destination testQueue = session.createQueue("drink");

            MessageConsumer consumer = session.createConsumer(testQueue);
            consumer.setMessageListener(message -> {
                try {
                    String text = ((TextMessage) message).getText();
                    System.err.println(text+"我听到了，我是尚硅谷北京总部飞龙老师");
                    System.out.println(Thread.currentThread().getId());

                    // session.commit();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    try {
                        session.rollback();
                    } catch (JMSException jmsException) {
                        jmsException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }

}
