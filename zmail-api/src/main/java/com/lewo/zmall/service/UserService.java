package com.lewo.zmall.service;

import com.lewo.zmall.model.UmsUser;

import java.util.List;

public interface UserService {
    public List<UmsUser> getAllUsers();

    UmsUser login(UmsUser umsUser);
}
