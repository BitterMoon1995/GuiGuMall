package com.lewo.zmail.test.core;

import com.lewo.zmail.test.diamond.Klee;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Dodoke implements Klee {
    @Override
    public String bomb() {
        return null;
    }

    @Override
    /*默认方法的好处：一个接口新增了一个方法，如果是默认方法，你可以不实现，也可以实现(重写)。
    反观抽象方法就必须实现，在接口的扩展性上有很大助益。*/
    public String whiteGirl() {
        return "klee!";
    }

    @Test
    public void test() {
        Dodoke dodoke = new Dodoke();
        System.out.println(dodoke.whiteGirl());
        System.out.println(Klee.nigger());
    }

    @Test
    /*再探F-I、lambda*/
    public void test2() {
        //匿名类与lambda
        Klee klee = () -> "klee get doomed";
        Klee baby = new Klee() {
            @Override
            public String bomb() {
                return "小乖宝";
            }
        };
        System.out.println(klee.bomb());
        System.out.println(baby.bomb());
    }

    @Test
    public void test3() {
        //匿名类触摸Consumer
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        };
        consumer.accept(8848);
    }

    @Test
    public void test4() {
        //触摸Predicate
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 8;
            }
        };
        System.out.println(predicate.test(9));

        //lambda等效写法
        Predicate<Integer> smart = integer -> integer > 9;
        System.out.println(smart.test(9));
    }

    /*Lambda表达的内容被编译成了当前类的一个静态或实例方法...
    ...执行MethodHanlde代表的方法(?), 返回结果, 结果为动态生成的接口实例, 接口实现调用1步中生成的方法
    也就是说实际上，像这样把函数式接口作为参数的方法，他实际并不是接收一个接口，
    而是一个用lambda表达式编写的、在JVM动态生成的接口实例。
    这种方法看起来只是起到一个传递形参的作用，并不优雅，中国人的败类！*/
    public String sima(Integer x, Function<Integer, String> function) {
        return function.apply(x);
    }

    @Test
    public void test5() {
        String simaRes = sima(-2, integer -> integer > 0 ? "大于0！" : "并不家人们");
        System.out.println(simaRes);
        //日式の优雅
        Function<Integer, String> function = integer -> integer > 9 ? "大于！" : "不大于！";
        String smartRes = function.apply(9);
        System.out.println(smartRes);
    }

}
