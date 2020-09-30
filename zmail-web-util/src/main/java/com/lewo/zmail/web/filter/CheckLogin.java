package com.lewo.zmail.web.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//限定该注解的使用位置:方法
@Target(ElementType.METHOD)
//想要被反射读取，只能
@Retention(RetentionPolicy.RUNTIME)
//该handler方法（就控制器的方法）需不需要检查用户的登录状态
public @interface CheckLogin {
    //方法是否要求必须登录
    boolean mustLogin() default true;
}
