package com.lewo.zmall.service;

import com.lewo.zmall.model.UmsUserReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserAddressService {
    List<UmsUserReceiveAddress> getUserAddress(String userId);
}
