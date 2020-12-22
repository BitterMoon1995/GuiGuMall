package com.lewo.zmail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import tk.mybatis.spring.annotation.MapperScan;

@EnableJms//采取最终一致性+补偿性机制
@EnableDubbo
@MapperScan(basePackages = "com.lewo.zmail.pay.mapper")
@SpringBootApplication
public class Alipay {
    public static void main(String[] args) {
        SpringApplication.run(Alipay.class);
    }
}
