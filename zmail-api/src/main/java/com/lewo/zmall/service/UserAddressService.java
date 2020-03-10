package com.lewo.zmall.service;

import com.lewo.zmall.model.UserAddress;

import java.util.List;

public interface UserAddressService {

    List<UserAddress> getAddressById(Long id);
}
