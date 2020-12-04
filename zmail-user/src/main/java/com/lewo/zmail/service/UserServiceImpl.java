//package com.lewo.zmail.user.service;
//
//import com.lewo.zmail.user.db.UserMapper;
//import com.lewo.zmall.model.UmsUser;
//import com.lewo.zmall.service.UserService;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//@DubboService
//public class UserServiceImpl  implements UserService {
//    @Autowired
//    UserMapper userMapper;
//    public List<UmsUser> getAllUsers(){
//        return userMapper.selectAll();
//    }
//}
