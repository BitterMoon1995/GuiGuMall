package com.lewo.zmail.service;

import com.alibaba.fastjson.JSONObject;
import com.lewo.zmail.db.UserMapper;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    //与验证中心交互，不对外暴露
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

    public void storeToken(String token,String userId){
        redisTemplate.opsForValue().set("token:" + token + ":userId",userId);
    }

    @Override
    @Transactional
    public UmsUser loginFromWeibo(String userJson, String access_token, String access_code) {
        Map<String,Object> userMap = JSONObject.parseObject(userJson, Map.class);
        UmsUser user = new UmsUser();

        //校验该微博注册账户是否已存在
        String id = userMap.get("id").toString();
        user.setSourceUid(id);
        UmsUser existed = mapper.selectOne(user);
        if (existed != null)
            return existed;

        //不存在，执行注册逻辑
        String province = userMap.get("province").toString();
        int gender = 88;
        switch (userMap.get("gender").toString()){
            case "m" : gender = 1;
            break;
            case "f" : gender = 2;
            break;
            default : gender = 0;
        }
        String coordinate = userMap.get("location").toString();
        String name = userMap.get("name").toString();
        String profileImg = userMap.get("profile_image_url").toString();

        //预设值
        user.setSourceType(2);
        user.setAccessCode(access_code);
        user.setAccessToken(access_token);

        user.setGender(gender);
        user.setCoordinate(coordinate);//位置信息
        user.setNickname(name);
        user.setProfileImg(profileImg);
        user.setCreateTime(new Date());
        user.setProvince(province);

        /*
        一个极极极小的【细节】：这个user插入后才生成主键id
        但是调用方（认证中心-第三方登陆接口）此时随后就需要userID来生成token
        所以插入后就要查出来，把ID拿到，给要返回的user对象
         */
        mapper.insert(user);
        UmsUser inserted = mapper.selectOne(user);
        user.setId(inserted.getId());
        return user;
    }


}
