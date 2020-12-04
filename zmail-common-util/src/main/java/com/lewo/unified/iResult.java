package com.lewo.unified;

import lombok.Data;

import java.io.Serializable;

@Data
public class iResult implements Serializable {
    private Object data;
    private Integer code;
    private Long total;
    private String msg;

    public static iResult success = new iResult(200,"操作成功");
    public static iResult serverDown = new iResult(520,"服务器开小差了ε=(´ο｀*)))");
    public static iResult illegalParam = new iResult(400,"非法参数");
    public static iResult emptyParam = new iResult(404,"空参数");
    public static iResult invalidAuth = new iResult(403,"无效权限");
    public static iResult dbException = new iResult(549,"持久层异常");
    public static iResult rpcException = new iResult(556,"远程调用异常");

    public iResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public iResult(Object data, Integer code, Long total, String msg) {
        this.data = data;
        this.code = code;
        this.total = total;
        this.msg = msg;
    }

    public static void main(String[] args) {
        System.out.println(new iResult(400,"sima"));
    }
}
