package com.github.superzhc.flink.manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 14:47
 */
@Data
@Configuration
@PropertySource("classpath:custom.properties")
public class FlinkConfig {

    @Value("${flink.home}")
    private String home;

    public String flinkShell() {
        return home + "/bin/flink";
    }
}
