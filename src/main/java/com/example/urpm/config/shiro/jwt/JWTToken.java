package com.example.urpm.config.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 12:39
 * @description JwtToken
 */
public class JWTToken implements AuthenticationToken {

    /**
     * Token
     */
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
