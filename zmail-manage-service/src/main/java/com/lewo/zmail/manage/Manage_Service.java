package com.lewo.zmail.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lewo.zmail.manage.dao")
@SpringBootApplication
public class Manage_Service {
    public static void main(String[] args) {
        SpringApplication.run(Manage_Service.class,args);
    }
}
