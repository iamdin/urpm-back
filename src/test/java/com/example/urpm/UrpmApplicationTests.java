package com.example.urpm;

import com.example.urpm.model.common.Constant;
import com.example.urpm.util.JedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UrpmApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void jedis() {
        JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + "root");
    }

}
