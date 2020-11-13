package com.lewo.unified;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class iResult implements Serializable {
    private Status status;
    private Object data;
    private long total;//分页必用

    //私有静态对象，用于仅传输请求状态信息
    //采用饿汉式，类加载时只创建一次，天生线程安全，且代码简洁
    public static iResult success = new iResult(Status.success);
    public static iResult serverDown = new iResult(Status.serverDown);
    public static iResult illegalParam = new iResult(Status.illegalParam);
    public static iResult emptyParam = new iResult(Status.emptyParam);
    /*演示懒汉式，注意要考虑线程安全：
    1.volatile实例保证内存可见性、有序性（不保证原子性）
    2.实例化时，要双校验
    */
    public static volatile iResult invalidAuth = new iResult(Status.invalidAuth);

    public iResult(Status status, Object data, long total) {
        this.status = status;
        this.data = data;
        this.total = total;
    }

    public iResult(Status status, Object data) {
        this.status = status;
        this.data = data;
    }

    public iResult(Status status) {
        this.status = status;
    }

    public static iResult invalidAuth(){
        //判空
        if (invalidAuth == null){
            //类锁
            synchronized (iResult.class) {
                //二次校验
                if (invalidAuth == null)
                    invalidAuth = new iResult(Status.invalidAuth);
            }
        }
        return invalidAuth;
    }
}
