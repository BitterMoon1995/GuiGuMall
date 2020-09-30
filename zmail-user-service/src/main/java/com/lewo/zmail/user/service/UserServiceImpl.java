package com.lewo.zmail.user.service;

import com.alibaba.fastjson.JSON;
import com.lewo.zmail.user.db.UserMapper;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService
public class UserServiceImpl  implements UserService {
    @Autowired
    UserMapper mapper;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    public List<UmsUser> getAllUsers(){
        return mapper.selectAll();
    }

    @Override
    public UmsUser login(UmsUser umsUser) {
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        String username = umsUser.getUsername();
        String password = umsUser.getPassword();
        //小问题：Redis服务器down了怎么办？或者说，我如何知道有没有down？
        String realPassword = (String) value.get("user:" + username + ":password");
        //缓存没有
        if (StringUtils.isBlank(realPassword)){
            //查数据库
            UmsUser query = new UmsUser();
            query.setUsername(username);
            query.setPassword(password);
            UmsUser dbRes = mapper.selectOne(query);
            //数据库也没有
            if (dbRes==null)
                return null;
            //数据库有，把username-password username-(user)info同步到缓存
            else {
                value.set("user:"+username+":password",dbRes.getPassword(),12, TimeUnit.HOURS);
                value.set("user:"+username+":info",dbRes,12,TimeUnit.HOURS);
                return dbRes;
            }
        }
        //缓存有密码，别忘了还要校验传过来的密码
        else {
            //如果传的密码和真密码匹配，才返信息
            if (password.equals(realPassword))
                //缓存只要有密码，就一定有用户信息。直接转，不用fastJson
                return (UmsUser) value.get("user:" + username + ":info");
            else return null;
        }
    }
}
