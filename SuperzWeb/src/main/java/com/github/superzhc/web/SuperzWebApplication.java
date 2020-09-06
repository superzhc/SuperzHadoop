package com.github.superzhc.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.superzhc.web.mapper")
public class SuperzWebApplication
{

    public static void main(String[] args) {
        SpringApplication.run(SuperzWebApplication.class, args);
    }

}
