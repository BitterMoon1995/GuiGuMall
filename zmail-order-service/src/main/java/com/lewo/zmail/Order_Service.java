package com.lewo.zmail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import tk.mybatis.spring.annotation.MapperScan;

@EnableJms
@EnableCaching
@EnableDubbo
@MapperScan(basePackages = "com.lewo.zmail.order.db")
@SpringBootApplication
public class Order_Service {
    public static void main(String[] args) {
        SpringApplication.run(Order_Service.class);
    }
}
