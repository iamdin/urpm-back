package com.example.urpm.util;

import com.example.urpm.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 21:11
 * @description Jedis 操作工具类
 */
@Slf4j
@Component
public class JedisUtil {

    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
    }


    /**
     * 获取Jedis实例
     * @return
     */
    public static synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException("获取Jedis资源异常:" + e.getMessage());
        }
    }

    /**
     * 获取指定Key的Value
     * @param key
     * @return String
     */
    public static String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis != null ? jedis.get(key) : null;
        } catch (Exception e) {
            log.error("redis get key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("获取Redis键值get方法异常: key=" + key + " errorMessage=" + e.getMessage());
        }
    }


    public static void set(String key, String value, int expiretime) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.set(key, value).equals("OK")) {
                jedis.expire(key, expiretime);
            }
        } catch (Exception e) {
            log.error("redis set key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("设置Redis键值setJsonObject方法异常: key=" + key +
                    " value=" + value +
                    " expiretime=" + expiretime +
                    " errorMessage=" + e.getMessage());
        }
    }


    /**
     * 删除 key
     * @param key
     */
    public static void delKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            log.error("redis set key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("删除Redis键值delKey方法异常: key=" + key + " errorMessage=" + e.getMessage());
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("redis exists key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("判断Redis键值exists方法异常: key=" + key + " errorMessage=" + e.getMessage());
        }
    }

}
