package com.example.urpm.util;

import com.example.urpm.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 21:11
 * @description Jedis 操作工具类
 */
@Slf4j
@Component
public class JedisUtil {

    private final JedisPool jedisPool;

    public JedisUtil(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized Jedis getJedis() {
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
     * jedis放回连接池
     * @param jedis
     */
    public void close(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            log.error("close redis error :{}", e.getMessage());
            throw new BusinessException("放回 Jedis 连接池异常:" + e.getMessage());
        }

    }

    /**
     * 获取指定Key的Value
     * @param key
     * @return String
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis != null ? jedis.get(key) : null;
        } catch (Exception e) {
            log.error("redis get key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("获取Redis键值get方法异常: key=" + key + " errorMessage=" + e.getMessage());
        } finally {
            close(jedis);
        }
    }


    public void set(String key, String value, int expiretime) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.requireNonNull(jedis).set(key, value).equals("OK")) {
                jedis.expire(key, expiretime);
            }
        } catch (Exception e) {
            log.error("redis set key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("设置Redis键值setJsonObject方法异常: key=" + key +
                    " value=" + value +
                    " expireTime=" + expiretime +
                    " errorMessage=" + e.getMessage());
        } finally {
            close(jedis);
        }
    }


    /**
     * 删除 key
     * @param key
     */
    public void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Objects.requireNonNull(jedis).del(key);
        } catch (Exception e) {
            log.error("redis set key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("删除Redis键值delKey方法异常: key=" + key + " errorMessage=" + e.getMessage());
        } finally {
            close(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return Objects.requireNonNull(jedis).exists(key);
        } catch (Exception e) {
            log.error("redis exists key Exception: key=" + key + " errorMessage=" + e.getMessage());
            throw new BusinessException("判断Redis键值exists方法异常: key=" + key + " errorMessage=" + e.getMessage());
        } finally {
            close(jedis);
        }
    }

}
