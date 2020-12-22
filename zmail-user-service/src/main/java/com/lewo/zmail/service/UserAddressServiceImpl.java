package com.lewo.zmail.service;

import com.lewo.exception.DbException;
import com.lewo.zmail.db.UserAddressMapper;
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

    @Override
    public UmsUserReceiveAddress getById(String addrId) {
        UmsUserReceiveAddress receiveAddress;
        try {
            receiveAddress = addressMapper.selectByPrimaryKey(addrId);
        } catch (Exception e) {
            throw new DbException("额额");
        }
        return receiveAddress;
    }

}
