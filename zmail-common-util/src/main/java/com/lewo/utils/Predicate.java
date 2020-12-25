package com.lewo.utils;

import com.lewo.zmall.unified.Code;
import com.lewo.zmall.unified.iResult;

/**
 * 对调用结果进行判断的判断类
 */
public class Predicate {

    //快速判断返回结果
    public static boolean suc(iResult result){
        return result.getCode() == Code.success;
    }
    public static boolean fail(iResult result){
        return result.getCode() != Code.success;
    }
}
