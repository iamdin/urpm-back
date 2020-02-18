package com.example.urpm.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 19:34
 * @description RedisConfiguration
 */
@Slf4j
@Configuration
public class RedisConfiguration {

    private final RedisProperties properties;

    public RedisConfiguration(RedisProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JedisPool jedisPool() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(properties.getJedis().getPool().getMaxActive());
            jedisPoolConfig.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
            jedisPoolConfig.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
            String host = properties.getHost();
            int port = properties.getPort();
            int timeout = (int) properties.getTimeout().toMillis();
            String password = properties.getPassword();
            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            log.debug(" ==> Jedis Config host:{},password:{}", host, password);
            log.debug(" <====  Initialize JedisPool Success !");
            return jedisPool;
        } catch (Exception e) {
            log.error("====>  Initialize JedisPool Failed!");
            log.error(e.getMessage());
        }
        return null;
    }
}
