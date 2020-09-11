package com.lewo.zmail.user.service;

import com.lewo.zmail.user.dao.UserAddressMapper;
import com.lewo.zmall.model.UserAddress;
import com.lewo.zmall.service.UserAddressService ;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@DubboService
public class UserAddressServiceImpl implements UserAddressService{
    @Autowired
    UserAddressMapper mapper;
    @Override
    public List<UserAddress> getAddressById(Long id) {

        Example example = new Example(UserAddress.class);
        example.createCriteria().andEqualTo("memberId",id);
        List<UserAddress> userAddresses = mapper.selectByExample(example);//必须传example

        UserAddress userAddress2 = new UserAddress();
        userAddress2.setMemberId(id);
        List<UserAddress> userAddresses2 = mapper.select(userAddress2);

        return userAddresses;


    }
}
