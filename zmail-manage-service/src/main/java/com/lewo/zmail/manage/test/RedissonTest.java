package com.lewo.zmail.manage.test;

import com.lewo.zmail.manage.config.RedissonConfig;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

public class RedissonTest {
    @Autowired
    RedissonClient redissonClient;

    public String testRss(){
        RLock lock = redissonClient.getLock("死你妈");
        return "死你妈逼";
    }
}
