package com.lewo.zmail.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDubbo
@EnableCaching
@MapperScan(basePackages = "com.lewo.zmail.user.db")//使用通用mapper的mapperScan
@SpringBootApplication
public class User_Service {

    public static void main(String[] args) {
        SpringApplication.run(User_Service.class, args);
    }

}
