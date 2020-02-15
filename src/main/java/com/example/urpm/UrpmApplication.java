package com.example.urpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.example.urpm.mapper")
@SpringBootApplication
public class UrpmApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrpmApplication.class, args);
    }

}
