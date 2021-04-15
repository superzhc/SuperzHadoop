package com.github.superzhc.flink.manage.job.builder.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.flink.manage.config.BigDataConfig;
import com.github.superzhc.flink.manage.config.JarPackagesConfig;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;
import com.github.superzhc.flink.manage.job.builder.JobBuilder;
import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunCLI;
import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunCLIOptions;
import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunDefaultModeOptions;
import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunYarnClusterModeOptions;
import com.github.superzhc.flink.manage.job.cli.parse.FlinkRunCLIParse;
import com.github.superzhc.flink.manage.job.packages.JobPackagesPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/13 15:12
 */
@Component("flink")
public class FlinkJobBuilder implements JobBuilder {

    @Autowired
    private BigDataConfig bigDataConfig;

    @Autowired
    private JarPackagesConfig jarPackagesConfig;

    @Override
    public List<String> build(JobConfigVO jobConfigVO) {
        FlinkRunCLI flinkRunCLI = new FlinkRunCLI();
        flinkRunCLI.setFlink(bigDataConfig.flinkShell());
        // 设置Jar包的地址
        flinkRunCLI.setJarFile(jarPackagesConfig.uploadPath(JobPackagesPath.parse(jobConfigVO.getJobPackagePath()).path()));
        flinkRunCLI.setArguments(jobConfigVO.getJobArguments());
        // 参数处理
        FlinkRunCLIOptions options;
        if ("yarn-cluster".equals(jobConfigVO.getJobMode())) {
            options = new FlinkRunYarnClusterModeOptions();
        } else {
            options = new FlinkRunDefaultModeOptions();
        }
        options.setClassname(jobConfigVO.getJobMainClass());
        JSONObject optionsObj = JSON.parseObject(jobConfigVO.getJobOptions());
        try {
            for (Map.Entry<String, Object> entry : optionsObj.entrySet()) {
                if (!options.containOption(entry.getKey())) {
                    continue;
                }

                Field field = options.option(entry.getKey());
                field.setAccessible(true);
                field.set(options, entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("任务参数配置出错，请检查任务参数");
        }
        flinkRunCLI.setOptions(options);

        List<String> command = new FlinkRunCLIParse(flinkRunCLI).parse();
        return command;
    }
}
