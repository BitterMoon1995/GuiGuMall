package com.lewo.zmail.cart.service;

import org.junit.Test;

import java.math.BigDecimal;

public class LearnBigDecimal {
    @Test
    /*
    1.涉及到钱务必用BigDecimal
    2.BigDecimal务必用字符串初始化
     */
    public void test1() {
        BigDecimal floatBD = new BigDecimal(0.01f);
        System.out.println(floatBD);
        BigDecimal doubleBD = new BigDecimal(0.01d);
        System.out.println(doubleBD);
        BigDecimal strBD = new BigDecimal("0.01");
        System.out.println(strBD);
    }


}
