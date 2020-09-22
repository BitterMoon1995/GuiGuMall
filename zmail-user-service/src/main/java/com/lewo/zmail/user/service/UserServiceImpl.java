package com.lewo.zmail.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lewo.zmail.user.dao.UserMapper;
import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    UserMapper userMapper;
    public List<UmsUser> getAllUsers(){
        return userMapper.selectAll();
    }
}
