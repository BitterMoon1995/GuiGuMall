package littleSkirt.controller;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class RedissonController {

    @Autowired
    RedisTemplate<String,Object> template;

    @Autowired
    RedissonClient redissonClient;

    @ResponseBody
    @RequestMapping("testRedisson")
    public String testRedisson(){
        RLock lock = redissonClient.getLock("lock");// 声明锁
        lock.lock();//上锁
        try {
            ValueOperations op = template.opsForValue();
            String v = (String) op.get("k");
            if (StringUtils.isBlank(v)) {
                v = "1";
            }
            System.out.println("->" + v);
            op.set("k", (Integer.parseInt(v) + 1) + "");
        }finally {
            lock.unlock();// 解锁
        }
        return "维尼の残念";
    }

    @RequestMapping("testLock")
    public String testLock(){
        RLock lock = redissonClient.getLock("死你妈");
        lock.lock();
        System.out.println("操了");
        lock.unlock();
        return "success";
    }
}
