package com.lewo.zmail.user.service;

import com.lewo.zmail.user.dao.UmsUserReceiveAddressMapper;
import com.lewo.zmall.model.UmsUserReceiveAddress;
import com.lewo.zmall.service.UmsUserReceiveAddressService ;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@DubboService
public class UmsUserReceiveAddressServiceImpl implements UmsUserReceiveAddressService{
    @Autowired
    UmsUserReceiveAddressMapper mapper;
    @Override
    public List<UmsUserReceiveAddress> getAddressById(Long id) {

        Example example = new Example(UmsUserReceiveAddress.class);
        example.createCriteria().andEqualTo("userId",id);
        List<UmsUserReceiveAddress> userAddresses = mapper.selectByExample(example);//必须传example

        UmsUserReceiveAddress userAddress2 = new UmsUserReceiveAddress();
        userAddress2.setUserId(id);
        List<UmsUserReceiveAddress> userAddresses2 = mapper.select(userAddress2);

        return userAddresses;


    }
}
