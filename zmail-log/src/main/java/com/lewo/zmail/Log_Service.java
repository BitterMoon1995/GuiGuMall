package com.lewo.zmail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import tk.mybatis.spring.annotation.MapperScan;

@EnableJms
@EnableCaching
@EnableDubbo
@MapperScan(basePackages = "com.lewo.zmail.log.db")
@SpringBootApplication
public class Log_Service {
    public static void main(String[] args) {
        SpringApplication.run(Log_Service.class);
    }
}
