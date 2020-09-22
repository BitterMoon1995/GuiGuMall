package com.lewo.zmail.user.controller;

import com.lewo.zmall.model.UmsUserReceiveAddress;
import com.lewo.zmall.service.UmsUserReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UmsUserReceiveAddressController {
    @Autowired
    UmsUserReceiveAddressService userAddressService;
    @PostMapping("address/getById")
    public List<UmsUserReceiveAddress> getAddressById(Long id){
        System.out.println(id);
        List<UmsUserReceiveAddress> userAddresses=userAddressService.getAddressById(id);
        return userAddresses;
    }
}
