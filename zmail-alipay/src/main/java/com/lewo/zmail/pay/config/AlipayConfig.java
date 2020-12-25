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
    /*用户浏览器的同步通知地址
    * 如果不调alipayClient检查交易状态，那么会在用户付款、倒计时、跳转成功后被调用，
    * 如果用户在倒数完跳转之前关掉浏览器就麻了，
    * 所以不要在这个handler里面写回调业务！
    * 这个handler就是用来切一个安慰页面的
    * */
    public String return_payment_url;
    @Value("${pay_notify_url}")
    /*服务器的异步通知地址，真正可靠的成功回调地址，与用户的行为无关
    * 沙箱环境无效（不得调你）
    * 玩沙箱的时候假装上面那个是这个吧*/
    public String notify_payment_url;
    @Value("${order_notify_url}")
    public String return_order_url;
    @Value("${alipay_public_key}")
    public String alipay_public_key;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(alipay_url, app_id, app_private_key, format, charset, alipay_public_key, sign_type);
    }
}