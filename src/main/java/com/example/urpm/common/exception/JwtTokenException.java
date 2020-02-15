package com.example.urpm.common.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author dingjinyang
 * @datetime 2020/2/14 10:53
 * @description Jwt token过期异常
 */
public class JwtTokenException extends AuthenticationException {
    public JwtTokenException(String msg) {
        super(msg);
    }

    public JwtTokenException() {
        super();
    }
}
