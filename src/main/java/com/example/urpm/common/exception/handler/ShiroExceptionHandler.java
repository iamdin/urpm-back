package com.example.urpm.common.exception.handler;

import com.example.urpm.common.base.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author dingjinyang
 * @datetime 2020/2/14 8:42
 * @description Shiro 相关异常捕获
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ShiroExceptionHandler {


    /**
     * 捕获所有shiro异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public RestResult<Object> shiroException(ShiroException e) {
        return new RestResult<>(HttpStatus.UNAUTHORIZED, "无权访问(Unauthorized):" + e.getMessage());
    }


    /**
     * 捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public RestResult<Object> unauthorizedException(UnauthorizedException e) {
        return new RestResult<>(HttpStatus.UNAUTHORIZED, "无权访问(Unauthorized):当前Token没有此请求所需权限(" + e.getMessage() + ")");
    }

    /**
     * 以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public RestResult<Object> handle401(UnauthenticatedException e) {
        return new RestResult<>(HttpStatus.UNAUTHORIZED, "认证失败，无权访问(Unauthorized)");
    }
}
