package com.lewo.zmail.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lewo.zmall.model.UserAddress;
import com.lewo.zmall.service.UserAddressService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAddressController {
    @DubboReference
    UserAddressService userAddressService;
    @PostMapping("address/getById")
    public List<UserAddress> getAddressById(Long id){
        System.out.println(id);
        List<UserAddress> userAddresses=userAddressService.getAddressById(id);
        return userAddresses;
    }
}
