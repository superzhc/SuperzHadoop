package com.github.superzhc.flink.manage.job.executor.impl;

import ch.ethz.ssh2.Connection;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import com.github.superzhc.flink.manage.config.BigDataConfig;
import com.github.superzhc.flink.manage.config.JarPackagesConfig;
import com.github.superzhc.flink.manage.job.executor.JobExecutor;
import com.github.superzhc.flink.manage.job.packages.JobPackages;
import com.github.superzhc.flink.manage.job.packages.JobPackagesPath;
import com.github.superzhc.flink.manage.util.CommandInputStreamConsumer;
import com.github.superzhc.flink.manage.util.SSH2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/4/13 16:07
 */
@Slf4j
public class RemoteJobExecutor implements JobExecutor {
    @Autowired
    private BigDataConfig bigDataConfig;

    @Autowired
    private JarPackagesConfig jarPackagesConfig;

    @Autowired
    private JobPackages jobPackages;

    @Override
    public String uploadJar(String path) {
        Connection conn = null;
        try {
            // 上传文件保存的路径
            String uploadPath = jarPackagesConfig.uploadPath(JobPackagesPath.parse(path).path());

            // 文件内容
            InputStream in = jobPackages.get(path);

            conn = SSH2Util.openConnection(bigDataConfig.getHost(), Integer.parseInt(bigDataConfig.getPort()), bigDataConfig.getUsername(), bigDataConfig.getPassword());
            SSH2Util.upload(conn, uploadPath, in);
            IoUtil.close(in);
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return null;
    }

    @Override
    public String execute(List<String> command) {
        Connection conn = null;
        try {
            conn = SSH2Util.openConnection(bigDataConfig.getHost(), Integer.parseInt(bigDataConfig.getPort()), bigDataConfig.getUsername(), bigDataConfig.getPassword());

            CommandInputStreamConsumer responseConsumer=new CommandInputStreamConsumer(new ArrayList<>());
            int result = SSH2Util.exec(conn, CollectionUtil.join(command, " "), responseConsumer);
            return CollectionUtil.join(responseConsumer.getContainer(),"\n");
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return null;
    }


}
