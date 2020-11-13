package com.lewo.zmail.user.service;

import com.lewo.zmail.user.db.UserAddressMapper;
import com.lewo.zmall.model.UmsUserReceiveAddress;
import com.lewo.zmall.service.UserAddressService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@DubboService
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    UserAddressMapper addressMapper;

    @Override
    public List<UmsUserReceiveAddress> getUserAddress(String userId) {
        UmsUserReceiveAddress address = new UmsUserReceiveAddress();
        address.setUserId(userId);
        return addressMapper.select(address);
    }
}
