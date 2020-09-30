package com.lewo.zmail.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//服务端跨域配置
//非简单请求  非简单请求是那种对服务器有特殊要求的请求，比如请求方法是PUT或DELETE，
// 或者Content-Type字段的类型是application/json。非简单请求的CORS请求，
// 会在正式通信之前，增加一次HTTP查询请求，称为"预检"请求（preflight）。

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // 添加CORS配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Access-Control-Allow-Methods：必选
        //它的值是逗号分隔的一个字符串，表明服务器支持的所有跨域请求的方法。
        // 注意，返回的是所有支持的方法，而不单是浏览器请求的那个方法。这是为了避免多次"预检"请求。
        corsConfiguration.addAllowedMethod("*");

        // Access-Control-Allow-Origin：必选
        // 它的值要么是请求时Origin字段的值，要么是一个*，表示接受任意域名的请求。
        // 要么指定好源：协议、域名（不是IP！）、端口号
        corsConfiguration.addAllowedOrigin ("*");

        // Access-Control-Allow-Credentials：可选
        // 它的值是一个布尔值，表示是否允许发送Cookie。默认情况下，Cookie不包括在CORS请求之中。
        // 设为true，即表示服务器明确许可，Cookie可以包含在请求中，一起发给服务器。
        // 这个值也只能设为true，如果服务器不要浏览器发送Cookie，删除该字段即可。
        corsConfiguration.setAllowCredentials(true);

        // 如果浏览器请求包括Access-Control-Request-Headers字段，则
        // Access-Control-Allow-Headers字段是必需的。它也是一个逗号分隔的字符串，
        // 表明服务器支持的所有头信息字段，不限于浏览器在"预检"中请求的字段。
        corsConfiguration.addAllowedHeader("*");

        // (简单请求)
        // Access-Control-Expose-Headers：可选
        // CORS请求时，XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段：
        // Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma。
        // 如果想拿到其他字段，就必须在Access-Control-Expose-Headers里面指定。
        // 这里的例子指定，getResponseHeader(‘NIGGER’)可以返回NIGGER字段的值。
        corsConfiguration.addExposedHeader(HttpHeaders.ACCEPT);

        // 添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

}
