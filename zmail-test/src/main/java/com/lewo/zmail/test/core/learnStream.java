package com.lewo.zmail.test.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class learnStream {
    @Test
    public void test() {
        int[] ints = IntStream
                .rangeClosed(0, 10)
                .filter(value -> value % 2 == 0)
                .toArray();
        /*不是collect(Collectors.toList());
        * IntStream 和 Steam是兄弟接口*/
        System.out.println(Arrays.toString(ints));
    }
}
