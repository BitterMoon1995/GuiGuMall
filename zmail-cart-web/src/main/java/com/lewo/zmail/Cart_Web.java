package com.lewo.zmail;
/*
！注意如果web项目想要访问到web-util配置的spring组件（比如AuthInterceptor认证拦截器以及WebConfiguration配置类）
那么启动类就【并不能】放在com.lewo.zmail.XXX(模块名)下，
而必须放在com.lewo.zmail下
 */
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class Cart_Web {
    public static void main(String[] args) {
        SpringApplication.run(Cart_Web.class,args);
    }
}
