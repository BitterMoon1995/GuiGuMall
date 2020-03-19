package com.lewo.zmail.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.User;
import com.lewo.zmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Reference
    UserService userService;
    @GetMapping("/index")
    public String index(){
        return "小气残忍";
    }
    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
