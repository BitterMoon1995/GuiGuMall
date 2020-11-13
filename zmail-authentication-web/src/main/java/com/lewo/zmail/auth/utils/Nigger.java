package com.lewo.zmail.auth.utils;

import com.lewo.zmall.model.UmsUser;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Nigger {
    public static void main(String[] args) {
        ThreadLocal<UmsUser> threadLocal = new ThreadLocal<>();
        UmsUser user = new UmsUser();
        user.setPassword("52meiqun");
        threadLocal.set(user);

        UmsUser user1 = threadLocal.get();
        System.out.println(user1);
    }
}
