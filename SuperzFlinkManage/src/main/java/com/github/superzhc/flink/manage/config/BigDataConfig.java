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
@PropertySource("classpath:bigdata.properties")
public class BigDataConfig {
    @Value("${job.submit.mode}")
    private String jobSubmitMode;

    @Value("${flink.server.host}")
    private String host;

    @Value("${flink.server.port}")
    private String port;

    @Value("${flink.username}")
    private String username;

    @Value("${flink.password}")
    private String password;

    @Value("${flink.home}")
    private String home;

    @Value("${yarn.rm.http.address}")
    private String yarnRmHttpAddress;

    public String flinkShell() {
        return home + "/bin/flink";
    }
}
