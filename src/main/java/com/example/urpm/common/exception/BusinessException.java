package com.example.urpm.common.exception;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 11:35
 * @description 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException() {
        super();
    }
}
