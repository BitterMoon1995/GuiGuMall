package com.lewo.zmail.manage;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@EnableDubbo
@MapperScan(basePackages = "com.lewo.zmail.manage.dao")
@SpringBootApplication
public class Manage_Service {
    public static void main(String[] args) {
        SpringApplication.run(Manage_Service.class,args);
    }
}
