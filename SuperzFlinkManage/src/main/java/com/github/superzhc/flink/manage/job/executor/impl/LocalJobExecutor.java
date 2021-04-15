package com.github.superzhc.flink.manage.job.executor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.github.superzhc.flink.manage.config.JarPackagesConfig;
import com.github.superzhc.flink.manage.job.executor.JobExecutor;
import com.github.superzhc.flink.manage.job.packages.JobPackages;
import com.github.superzhc.flink.manage.job.packages.JobPackagesPath;
import com.github.superzhc.flink.manage.util.CommandInputStreamConsumer;
import com.github.superzhc.flink.manage.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地执行
 *
 * @author superz
 * @create 2021/4/13 14:06
 */
@Slf4j
public class LocalJobExecutor implements JobExecutor {

    @Autowired
    private JarPackagesConfig jarPackagesConfig;

    @Autowired
    private JobPackages jobPackages;

    /**
     * 本地执行模式且本地存储文件则无需要上传，直接返回原地址即可
     *
     * @param path
     * @return
     */
    @Override
    public String uploadJar(String path) {
        JobPackagesPath jobPackagesPath = JobPackagesPath.parse(path);
        String uploadPath = jarPackagesConfig.uploadPath(jobPackagesPath.path());
        return jobPackages.downloadLocal(path,uploadPath);
    }

    @Override
    public String execute(List<String> command) {
        CommandInputStreamConsumer responseConsumer=new CommandInputStreamConsumer(new ArrayList<>());
        int result = ProcessUtil.exec(CollectionUtil.join(command," "),responseConsumer);
        return CollUtil.join(responseConsumer.getContainer(),"\n");
    }
}
