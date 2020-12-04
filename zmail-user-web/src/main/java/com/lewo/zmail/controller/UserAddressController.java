package com.lewo.zmail.controller;

import com.lewo.zmall.model.UmsUserReceiveAddress;
import com.lewo.zmall.service.UserAddressService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAddressController {
    @DubboReference
    UserAddressService userAddressService;
    @PostMapping("address/getById")
    public List<UmsUserReceiveAddress> getAddressById(String id){
        System.out.println(id);
        return userAddressService.getUserAddress(id);
    }
}
