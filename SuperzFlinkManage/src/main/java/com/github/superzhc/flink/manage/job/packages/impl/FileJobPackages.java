package com.github.superzhc.flink.manage.job.packages.impl;

import cn.hutool.core.io.FileUtil;
import com.github.superzhc.flink.manage.config.JarPackagesConfig;
import com.github.superzhc.flink.manage.job.packages.JobPackages;
import com.github.superzhc.flink.manage.job.packages.JobPackagesPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

/**
 * 本地文件方式
 *
 * @author superz
 * @create 2021/4/14 9:57
 */
@Slf4j
public class FileJobPackages implements JobPackages {
    @Autowired
    private JarPackagesConfig config;

    @Override
    public String saveTemp(String fileName, InputStream content) {
        String tempPath = config.fullTempPath(fileName);
        File file = FileUtil.writeFromStream(content, tempPath);
        return file.getPath();
    }

    @Override
    public String save(String packageName, String version, String fileName, InputStream content) {
        JobPackagesPath jobPackagesPath=new JobPackagesPath(packageName,version,fileName);
        String path = config.fullPath(jobPackagesPath.path());
        File file = FileUtil.writeFromStream(content, path);
        return file.getPath();
    }

    @Override
    public InputStream get(String path) {
        InputStream in = FileUtil.getInputStream(path);
        return in;
    }

    @Override
    public String downloadLocal(String source, String target) {
        if(source.equals(target)){
            return source;
        }
        File file=FileUtil.copy(source,target,true);
        return file.getPath();
    }

    @Override
    public boolean delete(String path) {
        boolean b = FileUtil.del(path);
        return b;
    }
}
