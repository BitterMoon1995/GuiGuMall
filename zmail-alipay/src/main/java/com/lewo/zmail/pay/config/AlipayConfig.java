package com.lewo.zmail.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @param
 * @return
 */
@Data
@Accessors(chain = true)
@Configuration
@PropertySource("classpath:alipay.properties")
public class AlipayConfig {
    @Value("${alipay_url}")
    private String alipay_url;
    @Value("${app_private_key}")
    private String app_private_key;
    @Value("${app_id}")
    private String app_id;
    public final static String format = "json";
    public final static String charset = "utf-8";
    public final static String sign_type = "RSA2";
    @Value("${pay_callback_url}")
    public String return_payment_url;//用户的同步通知
    @Value("${pay_notify_url}")
    public String notify_payment_url;//服务器的异步通知
    @Value("${order_notify_url}")
    public String return_order_url;
    @Value("${alipay_public_key}")
    public String alipay_public_key;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(alipay_url, app_id, app_private_key, format, charset, alipay_public_key, sign_type);
    }
}