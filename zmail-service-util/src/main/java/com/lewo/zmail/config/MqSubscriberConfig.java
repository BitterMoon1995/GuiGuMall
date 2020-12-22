package com.lewo.zmail.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.Session;

/**
 * 在使用activeMQ的过程当中，用@JmsListener修饰方法，给容器注册消费者 ，
 * 然后该注解只能玩p2p的模式，准确的说缺省装配的activeMQ就只支持p2p模式，
 * 除非在yml中配置spring.jms.pub-sub-domain=true，那么又只能使用pub/sub模式了，也不合适。
 *
 * 所以亲自配置，亲自注入一个JmsListenerContainerFactory，
 * 再在@JmsListener中配置containerFactory=该工厂对象名。
 *
 * 对类DefaultJmsListenerContainer的触摸：
 * 存在的意义是为每一个consumer创建一个TaskExecutor(juc)来运行监听线程，
 * 默认是SimpleAsyncTaskExecutor(spring.core.task)，(缺点：线程不能复用)
 * 核心方法是setTaskExecutor(Executor taskExecutor)
 */
@Configuration
public class MqSubscriberConfig {
    /*单独配置接收模式为发布-订阅模式的消息消费者(以后简称"订阅者")listener工厂
    * factory.setSubscriptionDurable()默认为false，这里没有改
    * 所以factory设置为本工厂的订阅者都是不能持久化的*/
    @Bean(name = "plainFactory")
    public DefaultJmsListenerContainerFactory plainListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.156.128:61616");

        factory.setConnectionFactory(activeMQConnectionFactory);
        /*就图这个*/
        factory.setPubSubDomain(true);
        /*要设为1，否则每次都会处理之前的所有消息*/
        factory.setMaxMessagesPerTask(1);
        /*设置并发数*/
        factory.setConcurrency("10");
        /*重连间隔时间*/
        factory.setRecoveryInterval(5000L);
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

        return factory;
    }
    /*持久化的消息订阅者使用的listener工厂*/
    @Bean(name = "durableFactory")
    public DefaultJmsListenerContainerFactory durableListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.156.128:61616");

        factory.setConnectionFactory(activeMQConnectionFactory);
        factory.setPubSubDomain(true);
        /*唯一区别：是否持久化设置为true*/
        factory.setSubscriptionDurable(true);
        /*You cannot create a durable subscriber without specifying a unique clientID*/
        factory.setClientId("blackNigger");
        factory.setMaxMessagesPerTask(1);
        factory.setConcurrency("10");
        factory.setRecoveryInterval(5000L);
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

        return factory;
    }
}
