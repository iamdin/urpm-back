package com.example.urpm;

import com.example.urpm.model.common.Constant;
import com.example.urpm.util.JedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UrpmApplicationTests {

    @Autowired
    private JedisUtil jedisUtil;

    @Test
    void contextLoads() {
    }

    @Test
    public void jedis() {
        jedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + "root");
    }

}
