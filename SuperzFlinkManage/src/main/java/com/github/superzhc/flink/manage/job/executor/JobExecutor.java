package com.github.superzhc.flink.manage.job.executor;

import java.util.List;

/**
 * 任务执行器
 *
 * @author superz
 * @create 2021/4/13 14:02
 */
public interface JobExecutor {
    String uploadJar(String path);

    String execute(List<String> command);
}
