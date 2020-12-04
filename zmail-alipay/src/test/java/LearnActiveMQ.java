import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jms.*;
public class LearnActiveMQ {
    @Test
    /*发布&订阅模型用topic*/
    public void pub_Sub() {

        ConnectionFactory connect = new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);// 开启事务

            Topic topic = session.createTopic("speech");// 话题模式的消息

            MessageProducer producer = session.createProducer(topic);

            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("可莉乖乖~\n");

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);


            session.commit();// 提交事务
            connection.close();//关闭链接

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    @Test
    /*点对点模型用queue*/
    public void pointToPoint() {
        ConnectionFactory connect = new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);// 开启事务

            Queue queue = session.createQueue("drink");

            MessageProducer producer = session.createProducer(queue);

            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("black slave");

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);


            session.commit();// 提交事务
            connection.close();//关闭链接

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
