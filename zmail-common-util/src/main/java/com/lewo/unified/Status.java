package com.lewo.unified;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态信息记录类
 * 枚举类找不到序列化方法，前端解析不出来，暂时这样吧
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private String msg;
    private int code;

    private static int success = 200;
    private static int invalidAuth = 403;
    private static int illegalParam = 400;
    private static int serverDown = 520;

    public static Status success(){
        return new Status("请求成功",success);
    }

    public static Status invalidAuth(){
        return new Status("无效权限",invalidAuth);
    }

    public static Status illegalParam(){
        return new Status("非法参数",illegalParam);
    }

    public static Status serverDown(){
        return new Status("服务器开小差啦w(ﾟДﾟ)w",serverDown);//
    }
}
