package com.lewo.zmail.cart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class Cart_Web {
    public static void main(String[] args) {
        SpringApplication.run(Cart_Web.class,args);
    }
}
