package com.lewo.zmail.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记！（指标记每个模块的入口handler method）
 * 如购物车模块的cartList(),订单模块的toTrade()，搜索模块的index()[也是商城首页商城首页]
 * 浏览器将跳转至这个模块的主页面并执行一系列初始化操作、业务逻辑
 * 暂时没有D用,只是方便看代码
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Entrance {
}
