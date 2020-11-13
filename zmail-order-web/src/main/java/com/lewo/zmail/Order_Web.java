package com.lewo.zmail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class Order_Web {
    public static void main(String[] args) {
        SpringApplication.run(Order_Web.class);
    }
}
