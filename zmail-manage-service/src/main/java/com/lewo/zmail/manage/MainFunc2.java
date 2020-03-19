package com.lewo.zmail.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lewo.zmail.manage.dao")
@SpringBootApplication
public class MainFunc2 {
    public static void main(String[] args) {
        SpringApplication.run(MainFunc2.class,args);
    }
}
