package com.github.superzhc.flink.manage.job.monitor;

/**
 * @author superz
 * @create 2021/4/12 19:11
 */
public interface JobMonitor {
    String status(String applicationId);
}
