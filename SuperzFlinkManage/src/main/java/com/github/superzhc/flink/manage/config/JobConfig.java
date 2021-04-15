package com.github.superzhc.flink.manage.config;

import cn.hutool.core.util.StrUtil;
import com.github.superzhc.flink.manage.job.executor.JobExecutor;
import com.github.superzhc.flink.manage.job.executor.impl.LocalJobExecutor;
import com.github.superzhc.flink.manage.job.executor.impl.RemoteJobExecutor;
import com.github.superzhc.flink.manage.job.packages.JobPackages;
import com.github.superzhc.flink.manage.job.packages.impl.FileJobPackages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author superz
 * @create 2021/4/13 17:19
 */
@Configuration
public class JobConfig {
    @Autowired
    private BigDataConfig bigDataConfig;

    @Autowired
    private JarPackagesConfig jarPackagesConfig;

    @Bean
    public JobPackages jobPackages() {
        if ("file".equals(jarPackagesConfig.getMode())) {
            return new FileJobPackages();
        } else {
            throw new IllegalArgumentException(StrUtil.format("配置文件[bigdata.properties]的配置项[jar.packages.mode]仅支持 file 模式"));
        }
    }

    @Bean
    public JobExecutor jobExecutor() {
        if ("local".equals(bigDataConfig.getJobSubmitMode())) {
            return new LocalJobExecutor();
        } else if ("remote".equals(bigDataConfig.getJobSubmitMode())) {
            return new RemoteJobExecutor();
        } else {
            throw new IllegalArgumentException(StrUtil.format("配置文件[bigdata.properties]的配置项[job.submit.mode]仅支持 local、remote 两个选项"));
        }
    }
}
