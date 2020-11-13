package com.lewo.zmall.service;

import com.lewo.zmall.model.UmsUser;
import com.lewo.zmall.model.UmsUserReceiveAddress;

import java.util.List;

public interface UserService {
    public List<UmsUser> getAllUsers();

    UmsUser login(UmsUser umsUser);

    void storeToken(String token,String userId);

    UmsUser loginFromWeibo(String userJson, String access_token, String access_code);

}
