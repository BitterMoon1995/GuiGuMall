package com.lewo.zmail.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lewo.zmail.user.dao")//使用通用mapper的mapperScan
@SpringBootApplication
public class ZmailUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZmailUserServiceApplication.class, args);
    }

}
