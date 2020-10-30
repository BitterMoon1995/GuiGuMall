package com.lewo.zmail.web.config;

import com.lewo.zmail.web.filter.AuthInterceptor;
import com.lewo.zmail.web.filter.CorsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                //老师坑点：静态资源不能拦截啊
                .excludePathPatterns("/login","/js/**","/css/**","/favicon/**","/icon/**",
                        "/png/**","/jpg/**","/font/**","/img/**","/image/**","/err/**","/error/**");
        registry.addInterceptor(new CorsInterceptor())
                .addPathPatterns("/**");
    }
}
