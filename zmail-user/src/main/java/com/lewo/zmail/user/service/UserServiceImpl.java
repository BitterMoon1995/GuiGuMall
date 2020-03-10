package com.lewo.zmail.user.service;

import com.lewo.zmail.user.dao.UserMapper;
import com.lewo.zmall.model.User;
import com.lewo.zmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    UserMapper userMapper;
    public List<User> getAllUsers(){
        return userMapper.selectAll();
    }
}
