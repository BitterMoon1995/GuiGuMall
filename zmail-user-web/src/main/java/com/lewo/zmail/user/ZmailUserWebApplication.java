package com.lewo.zmail.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
public class ZmailUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZmailUserWebApplication.class, args);
    }

}
