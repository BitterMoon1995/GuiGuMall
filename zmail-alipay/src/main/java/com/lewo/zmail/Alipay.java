package com.lewo.zmail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDubbo
@MapperScan(basePackages = "com.lewo.zmail.pay.mapper")
@SpringBootApplication
public class Alipay {
    public static void main(String[] args) {
        SpringApplication.run(Alipay.class);
    }
}
