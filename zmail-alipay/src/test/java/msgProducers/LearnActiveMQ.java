package msgProducers;

import com.lewo.zmall.service.AlipayService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.*;
import java.util.Iterator;
import java.util.ServiceLoader;

public class LearnActiveMQ {
    @Test
    /*发布&订阅模型用topic*/
    public void pub_Sub() {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        try {
            /*世界名画之《connectionFactory.createConnection()》
            连接（Connection）：连接是从客户端到服务实例(redis、mysql、mq......)的一条物理路径；
            会话（Session）：是通信双方从开始通信到通信结束期间的一个上下文（Context）。
            连接与会话是一对多的关系*/
            Connection connection = connectionFactory.createConnection();
            connection.start();
            /*消息签收模式：若开启了事务，那么只能为SESSION_TRANSACTED模式(设置其他的也无效)*/
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);// 开启事务

            Topic topic = session.createTopic("speech");// 话题模式的消息

            MessageProducer producer = session.createProducer(topic);

            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("支持不绝对，就是绝对不支持！——习近平总书记\n");
            /*消息传递的持久化机制：
            * 发布订阅模式，要在发送端设置，也就是如下；
            * 点对点模式，要在客户端设置→
            * */
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);
            /*通知模式为事务，则必须提交后的消息才能发出去，否则屁都没有（未提交的消息不会持久化）*/
            session.commit();
            connection.close();//关闭链接

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    @Test
    /*点对点模型用queue*/
    public void pointToPoint() {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.156.128:61616");
        try {
            Connection connection = factory.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);// 开启事务

            Queue queue = session.createQueue("drink");

            MessageProducer producer = session.createProducer(queue);

            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("黑色的、尼哥奴隶\n");

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);


            session.commit();// 提交事务
            connection.close();//关闭链接

        } catch (JMSException e) {
            e.printStackTrace();

        }
    }

    @Test
    public void test() {
        ServiceLoader<AlipayService> loader = ServiceLoader.load(AlipayService.class);
        System.out.println(loader.iterator().hasNext());
    }
}
/*
if (!transacted) {
            if (acknowledgeMode == Session.SESSION_TRANSACTED)(开启了事务就只能是事务会话模式)
                抛异常
            else if (acknowledgeMode 值<0或>4) (普通验非法参数)
                抛异常
        }
        return new ActiveMQSession(this, getNextSessionId()
        能玩明白么？↓
        , transacted ? Session.SESSION_TRANSACTED : acknowledgeMode
        , isDispatchAsync()
        , isAlwaysSessionAsync());
 */