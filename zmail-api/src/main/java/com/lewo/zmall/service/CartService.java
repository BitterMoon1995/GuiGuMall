package com.lewo.zmall.service;

import com.lewo.zmall.model.OmsCartItem;

import java.util.ArrayList;
import java.util.List;

public interface CartService {
    OmsCartItem getUsersItem(String userId, String skuId);

    void addUsersItem(OmsCartItem cartItem);

    void updateUsersItem(OmsCartItem usersItem);

    void flushCache(String userId);

    List<OmsCartItem> getUsersCart(String userId);

    void checkCart(String userId, Boolean isChecked, String productSkuId);
}
