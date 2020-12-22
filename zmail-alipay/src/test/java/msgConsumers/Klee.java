package msgConsumers;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**和activeMQ关系真不大，全是JMS的各种概念
 * 学习体会：确实不是给烂二本看的😅
 */
public class Klee {
    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER
                , ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.156.128:61616");
        try {
            Connection connection = connect.createConnection();
            /*设置client`s identifier以提供发布订阅模式下的持久化支持，
            * 该ID会被注册在MQ实例*/
            connection.setClientID("klee");
            connection.start();
            /*
               Session.AUTO_ACKNOWLEDGE  自动确认模式，一旦接收方应用程序的消息处理回调函数返回，
               会话对象就会确认消息的接收
               Session.CLIENT_ACKNOWLEDGE  客户端显式调用acknowledge方法手动签收
               Session.DUPS_OK_ACKNOWLEDGE 不必必须签收，消息可能会重复发送。
               在第二次重新传递消息的时候，消息头的JmsDelivered会被置为true标示当前消息已经传送过一次，
               客户端需要进行消息的重复处理控制。
             */
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            /*
            点对点模式
            消息发出后，所有正常运行consumer【轮询接收】每一条消息；
            如果所有consumer都关机或宕机，而provider采取的持久化传递模式，
            那么期间provider发送的消息会狂暴轰入最先恢复正常的consumer
            * */
            Queue testQueue = session.createQueue("drink");
            /*
            发布&订阅模式
            消息发出后，所有consumer【都会】接收到所有的消息；
            默认不能接收离线消息（provider端配置无效），必须设置clientID，
            且MessageConsumer采用createDurableSubscriber()构造
             */
            Topic testTopic = session.createTopic("speech");
            /*为连接设置clientID后↑，就可以构造持久化调阅者
            * 持久化订阅者可以接收离线消息*/
//            MessageConsumer klee = session.createDurableSubscriber(testTopic, "klee");
            MessageConsumer klee = session.createConsumer(testQueue);
            /*消费者同步接收，这里意为在10秒内，receiver阻塞，除非接收到来自发送方的下一个消息
            * 如果timeout设置为0或不设置，则receiver接收到消息之前永久阻塞*/
//            Message receive = consumer.receive(10000);
            /*异步接收*/
            klee.setMessageListener(message -> {
                try {
                    String text = ((TextMessage) message).getText();
                    System.err.println(text+"小乖宝听到了");
                    System.out.println(Thread.currentThread().getId());

                    // session.commit();非事务模式(transacted=false)此方法无意义
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
