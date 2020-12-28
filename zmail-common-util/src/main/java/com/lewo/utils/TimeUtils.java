package com.lewo.utils;

import java.time.*;
import java.util.Date;

public class TimeUtils {
    //考虑时区的情况，获取当前时间的date对象
    //calender高并发危险（？），禁用
    public static Date curTime(){
        ZoneId zoneId = ZoneId.of("GMT+8");
        LocalDateTime now0 = LocalDateTime.now();
        ZonedDateTime now = now0.atZone(zoneId);
        return Date.from(now.toInstant());
    }

    public static void main(String[] args) {
        LocalDateTime massacre;
        massacre = LocalDateTime.now().plusYears(1);
        System.out.println(massacre);
    }
    //LocalDate -> Date
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    //LocalDateTime -> Date
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    //Date -> LocalDate
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    //Date -> LocalDateTime
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant
                .ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
/*
这下支了，数据库连接的时区没设对，不能是UTC，那是祖国的时间！
就是说Java知道机子在冲国，系统时间是UTC+8/GMT+8
然而数据库连接URL配的是美国也就是UTC(0)
所以MySQL在插入数据的时候为了回归祖国，会把时间-8
解决方法：工具类和数据库连接配置含泪统一写GMT+8/GMT%2B8
https://blog.csdn.net/u010865811/article/details/83617225
 */