package com.lewo.zmail.pay.core;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

/**
 * 好像没dio用，又配又注入地，也customize不出个啥，徒增码量。
 * 直接jmsTemplate.convertAndSend("PAY_SUCCESS_QUEUE",map)
 * 字符串指定destination name就完事儿了。。。。。。
 */
@Configuration
public class MqQueue {
    @Bean
    public Queue myQueue(){
        return new ActiveMQQueue("drink");
    }
    @Bean
    public Queue thatNigger(){
        return new ActiveMQQueue("boss");
    }

    @Bean("paySucQueue")
    public Queue paySuccess(){
        return new ActiveMQQueue("PAYMENT_PAID_QUEUE");
    }
}
