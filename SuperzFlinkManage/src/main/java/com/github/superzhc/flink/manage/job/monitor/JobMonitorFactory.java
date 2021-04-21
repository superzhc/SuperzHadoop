package com.github.superzhc.flink.manage.job.monitor;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author superz
 * @create 2021/4/12 19:24
 */
@Service
public class JobMonitorFactory {

    @Autowired
    Map<String, JobMonitor> map = new ConcurrentHashMap<>();

    public JobMonitor getJobMonitor(String jobType) {
        JobMonitor jobMonitor = map.get(jobType);
        if (null == jobMonitor) {
            throw new RuntimeException(StrUtil.format("任务类型为[{}]尚未定义监控类，请实现 JobMonitor 接口！", jobType));
        }
        return jobMonitor;
    }

    public JobMonitor getYarnJobMonitor(){
        return getJobMonitor("yarn");
    }
}
