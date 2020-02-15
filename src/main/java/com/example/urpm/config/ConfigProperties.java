package com.example.urpm.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 17:11
 * @description 读取配置文件 config.properties
 */
@Configuration
@ConfigurationProperties(prefix = "properties")
public class ConfigProperties {

    public static String encryptAESKey;
    public static String encryptJWTKey;
    public static int accessTokenExpireTime;
    public static int refreshTokenExpireTime;
    public static int shiroCacheExpireTime;

    public void setEncryptAESKey(String encryptAESKey) {
        ConfigProperties.encryptAESKey = encryptAESKey;
    }

    public void setEncryptJWTKey(String encryptJWTKey) {
        ConfigProperties.encryptJWTKey = encryptJWTKey;
    }

    public void setAccessTokenExpireTime(int accessTokenExpireTime) {
        ConfigProperties.accessTokenExpireTime = accessTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(int refreshTokenExpireTime) {
        ConfigProperties.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public void setShiroCacheExpireTime(int shiroCacheExpireTime) {
        ConfigProperties.shiroCacheExpireTime = shiroCacheExpireTime;
    }
}
