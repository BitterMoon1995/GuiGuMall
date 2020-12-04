package com.lewo.zmail.test.service;

import com.lewo.zmall.model.UmsUser;
import org.springframework.stereotype.Service;

@Service
public class GardenOfTheWorld {
    public void testThreadLocal(ThreadLocal<UmsUser> threadLocal){
        UmsUser user = threadLocal.get();
        System.out.println(user);
    }
}
