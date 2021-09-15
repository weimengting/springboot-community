package com.community.life;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.community.life.mapper")
public class LifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeApplication.class, args);
    }

}
