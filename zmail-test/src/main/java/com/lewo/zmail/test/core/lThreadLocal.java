package com.lewo.zmail.test.core;

import com.lewo.zmall.model.UmsUser;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class lThreadLocal {
    public static void main(String[] args) {
        ThreadLocal<UmsUser> threadLocal = new ThreadLocal<>();
        try {
            UmsUser user = new UmsUser();
            user.setNickname("薇儿骚逼逼");
            user.setCreateTime(new Date());
            user.setId("薇儿 love nigger");
            threadLocal.set(user);

            func1(threadLocal);
            func2(threadLocal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadLocal.remove();
        }
    }
    static void func1(ThreadLocal<UmsUser> threadLocal){
        System.out.println("f1");
        System.out.println(threadLocal.get());
    }
    static void func2(ThreadLocal<UmsUser> threadLocal){
        System.out.println("f2");
        System.out.println(threadLocal.get());
    }
    @Test
    public void test() {
        Random random = new Random();
//        产生指定区间的有序IntStream，这里需要传入一个区间（左闭右开），产生的元素不包含最后一个。
        IntStream.range(0,5).forEach(value -> {
            new Thread(() -> {
                ThreadLocal<String> threadLocal = new ThreadLocal<>();
                threadLocal.set(value+" "+random.nextInt(5));
                System.out.println("线程和local值分别为："+threadLocal.get());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
