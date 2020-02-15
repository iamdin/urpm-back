package com.example.urpm.model.common;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 11:28
 * @description 常量
 */
public class Constant {

    /**
     * redis-key-前缀 shiro缓存
     */
    public static final String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * redis-key-前缀 授权Token
     */
    public static final String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

    /**
     * redis-key-前缀 权限刷新Token
     */
    public static final String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-account:
     */
    public static final String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    public static final String CURRENT_TIME_MILLIS = "currentTimeMillis";

}
