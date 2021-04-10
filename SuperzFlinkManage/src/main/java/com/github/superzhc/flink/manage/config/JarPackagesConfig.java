package com.github.superzhc.flink.manage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 15:54
 */
@Data
@Configuration
@ConfigurationProperties("jar.packages")
@PropertySource("classpath:custom.properties")
public class JarPackagesConfig {
    private String root;
}
