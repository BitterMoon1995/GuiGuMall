package com.lewo.zmail.cart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.lewo.zmail.cart.db")
@EnableCaching
@EnableDubbo
@SpringBootApplication
public class Cart_Service {
    public static void main(String[] args) {
        SpringApplication.run(Cart_Service.class,args);
    }
}
