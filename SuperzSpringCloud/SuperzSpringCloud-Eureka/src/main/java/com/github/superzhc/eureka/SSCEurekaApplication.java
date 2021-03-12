package com.github.superzhc.eureka;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 增加 @EnableEurekaServer 启动 Eureka 服务器的相关功能
 */
@EnableEurekaServer
@SpringBootApplication
public class SSCEurekaApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SSCEurekaApplication.class).run(args);
    }
}
