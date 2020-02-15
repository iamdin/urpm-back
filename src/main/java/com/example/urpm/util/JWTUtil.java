package com.example.urpm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.urpm.common.exception.BusinessException;
import com.example.urpm.common.exception.JwtTokenException;
import com.example.urpm.config.ConfigProperties;
import com.example.urpm.model.common.Constant;
import com.example.urpm.util.common.Base64ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 11:18
 * @description JWT 工具类
 */
@Slf4j
@Component
public class JWTUtil {

    /**
     * Token 校验
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, Constant.ACCOUNT) + Base64ConvertUtil.decode(ConfigProperties.encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            log.error("==> JWT verify : Exception ( {} )", e.getMessage());
            throw new JwtTokenException("JWT认证失败 --> " + e.getMessage());
        } catch (TokenExpiredException e) {
            log.error("==> JWT verify : TokenExpired ( {} )", e.getMessage());
            throw new JwtTokenException("JWT过期 --> " + e.getMessage());
        }
    }

    /**
     * 生成签名
     * @param account
     * @param currentTimeMillis
     * @return
     */
    public static String sign(String account, String currentTimeMillis) {
        try {
            // 帐号加JWT私钥加密
            String secret = account + Base64ConvertUtil.decode(ConfigProperties.encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + ConfigProperties.accessTokenExpireTime);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim("account", account)
                    .withClaim("currentTimeMillis", currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            log.error("JWTToken UnsupportedEncodingException:{}", e.getMessage());
            throw new BusinessException("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }

    /**
     * 获取Token信息
     * @param token String
     * @param claim String
     * @return String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error("Token getClaim Exception :{}", e.getMessage());
            throw new JWTDecodeException("Token有误！" + e.getMessage());
        }
    }
}
