package com.lewo.zmall.unified;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Code {
    public static int success = 200;
    public static int illegalParam = 400;
    public static int invalidAuth = 403;
    public static int emptyParam = 404;
    public static int serverDown = 500;
    public static int rpcExp = 512;
    public static int persistenceExp = 520;//Exp=Exception
    public static int cacheExp = 549;
    public static int MQExp = 558;//这是否
}
