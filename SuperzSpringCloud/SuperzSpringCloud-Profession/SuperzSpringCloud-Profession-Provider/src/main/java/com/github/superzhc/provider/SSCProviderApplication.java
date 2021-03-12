package com.github.superzhc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SSCProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSCProviderApplication.class,args);
    }
}
