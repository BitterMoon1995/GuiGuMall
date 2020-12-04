package com.lewo.zmail.test.diamond;

/**试试Java8标准的接口规范（指函数式接口、默认方法、静态方法）*/
@FunctionalInterface
public interface Klee {
    String bomb();

    /*静态方法、默认方法不是抽象的，不会触及函数式接口的限制*/
    static String nigger(){
        return "nigger";
    }
    default String whiteGirl(){
        return "whiteGirl";
    }
}
