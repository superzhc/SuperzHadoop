package com.github.superzhc.flink.manage.job.monitor;

import com.github.superzhc.flink.manage.job.monitor.entity.JobMonitorInfo;

/**
 * @author superz
 * @create 2021/4/12 19:11
 */
public interface JobMonitor {
    JobMonitorInfo info(String applicationId);

    String status(String applicationId);
}
