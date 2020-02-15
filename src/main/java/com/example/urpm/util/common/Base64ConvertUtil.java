package com.example.urpm.util.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 11:00
 * @description Base64 加密解密工具
 */
public class Base64ConvertUtil {

    /**
     * 加密
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes);
    }

    /**
     * 解密
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        return new String(decodeBytes);
    }

}
