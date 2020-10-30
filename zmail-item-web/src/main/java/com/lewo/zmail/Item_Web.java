package com.lewo.zmail;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubbo
@SpringBootApplication
public class Item_Web {

    public static void main(String[] args) {
        SpringApplication.run(Item_Web.class, args);
    }

}
