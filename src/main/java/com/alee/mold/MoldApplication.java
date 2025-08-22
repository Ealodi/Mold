package com.alee.mold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.alee.mold.mapper")
public class MoldApplication {

    public static void main(String[] args) {

        SpringApplication.run(MoldApplication.class, args);
    }

}
