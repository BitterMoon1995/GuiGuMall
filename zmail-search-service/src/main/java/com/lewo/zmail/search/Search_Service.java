package com.lewo.zmail.search;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class Search_Service {
    public static void main(String[] args) {
        SpringApplication.run(Search_Service.class,args);
    }
}
