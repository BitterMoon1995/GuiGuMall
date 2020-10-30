package com.lewo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRes {
    private int code;
    private String userId;
    private String nickname;

    public static int successCode = 200;
    public static int failCode = 444;
    //失败类，写死
    public static VerifyRes fail= new VerifyRes(failCode,"","");

    //全构造
    public VerifyRes(String userId, String nickname) {
        this.code = successCode;
        this.userId = userId;
        this.nickname = nickname;
    }

    public VerifyRes(int code) {
        this.code = code;
    }

    //成功类，不能写死
    public static VerifyRes success(String userId,String nickname){
        return new VerifyRes(successCode,userId,nickname);
    }
}
