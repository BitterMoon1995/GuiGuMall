package com.lewo.exception;

/**
 * 自定义异常之redis es kafka等中间件炸了
 */
public class MiddlewareException extends RuntimeException{
    public MiddlewareException() {
    }

    public MiddlewareException(String message) {
        super(message);
    }

    public MiddlewareException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiddlewareException(Throwable cause) {
        super(cause);
    }

    public MiddlewareException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
