package com.lewo.unified;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 状态信息记录类
 * 枚举类找不到序列化方法，前端解析不出来，暂时这样吧
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status implements Serializable {

    private String msg;
    private int code;

    public static int successCode = 200;
    public static int invalidAuthCode = 403;
    public static int illegalParamCode = 400;
    public static int emptyParamCode = 404;
    public static int serverDownCode = 520;

    public static Status success = new Status("请求成功",successCode);

    public static Status invalidAuth = new Status("无效权限",invalidAuthCode);

    public static Status illegalParam = new Status("非法参数",illegalParamCode);

    public static Status serverDown = new Status("服务器开小差啦w(ﾟДﾟ)w",serverDownCode);//

    public static Status emptyParam = new Status("空参数",emptyParamCode);

}
