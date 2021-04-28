package com.github.superzhc.flink.manage.job.monitor;

/**
 * @author superz
 * @create 2021/4/22 10:08
 */
public interface JobMonitorConstant {
    /**
     * 任务类型
     */
    String TYPE_MR="mapreduce";
    String TYPE_FLINK="flink";
    String TYPE_SPARK="spark";
    /**
     * 任务模式
     */
    String MODE_YARN="yarn";
    String MODE_STANDALONE="standalone";
    String MODE_MR="mapreduce";
    String MODE_K8S="k8s";
    String MODE_MESOS="mesos";
}
