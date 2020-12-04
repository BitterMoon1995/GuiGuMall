package com.lewo.zmail.web.global;

import com.lewo.exception.AuthException;
import com.lewo.exception.DbException;
import com.lewo.unified.Code;
import com.lewo.unified.iResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 偷梗血轮眼之增强控制器实现全局异常处理（普遍玩法）
 */
@ControllerAdvice
public class GlobalHandler {
    /*
    在普通的handler方法里面
     */
    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public iResult handleAuth(Exception e){
        if (StringUtils.isBlank(e.getMessage()))
            return iResult.invalidAuth;
        else
            return new iResult(Code.invalidAuth,e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DbException.class)
    public iResult handleDb(Exception e){
        /*想了想返给用户的还得是日常用语、套话
         * 日志记录的话自己想办法调日志服务
         * logService.record(e.getMsg(),Code.persistenceExp)......*/
        if (StringUtils.isBlank(e.getMessage()))
            return iResult.serverDown;
        else
            return new iResult(Code.serverDown,e.getMessage());
    }
}
