package MQconsumers;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Klee {
    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.156.128:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination testSpeech = session.createTopic("speech");

            MessageConsumer consumer = session.createConsumer(testSpeech);
            consumer.setMessageListener(message -> {
                if(message instanceof TextMessage){
                    try {
                        String text = ((TextMessage) message).getText();
                        System.err.println(text+"小乖宝听到了");
                        System.out.println(Thread.currentThread().getId());

                        // session.commit();
                        // session.rollback();
                    } catch (JMSException e) {
                        // TODO Auto-generated catch block
                        try {
                            session.rollback();
                        } catch (JMSException jmsException) {
                            jmsException.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
