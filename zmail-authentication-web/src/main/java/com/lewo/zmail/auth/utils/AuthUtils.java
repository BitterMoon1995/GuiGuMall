package com.lewo.zmail.auth.utils;

import com.lewo.unified.Constant;
import com.lewo.zmail.web.utils.JwtUtil;
import com.lewo.zmall.model.UmsUser;

import java.util.HashMap;

public class AuthUtils {
    public static String encodeUser(UmsUser user,String ip){
        HashMap<String, Object> encodeMap = new HashMap<>();
        encodeMap.put("userId",user.getId());
        encodeMap.put("nickname",user.getNickname());
        return JwtUtil.encode(Constant.jwtKey, encodeMap, ip);
    }
}
