package com.lewo.utils;

import com.lewo.unified.Code;
import com.lewo.unified.iResult;

public class Predicate {

    //快速判断返回结果
    public static boolean suc(iResult result){
        return result.getCode() == Code.success;
    }
}
