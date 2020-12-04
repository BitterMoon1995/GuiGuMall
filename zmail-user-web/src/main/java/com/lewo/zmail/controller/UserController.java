package com.lewo.zmail.controller;

import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.service.SkuService;
import com.lewo.zmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @DubboReference
    UserService userService;
    @DubboReference
    SkuService skuService;
    @GetMapping("/index")
    public String index(){
        return "小气残忍";
    }
    @GetMapping("/allUsers")
    public List<UmsUser> getAllUsers(){
        return userService.getAllUsers();
    }
}
