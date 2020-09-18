package com.lewo.zmail.search;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lewo.zmail.search.dao")
@EnableDubbo
@SpringBootApplication
public class Search_Service {
    public static void main(String[] args) {
        SpringApplication.run(Search_Service.class,args);
    }
}
