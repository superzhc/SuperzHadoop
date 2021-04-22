package com.github.superzhc.flink.manage.job.monitor.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 监控到的任务信息
 *
 * @author superz
 * @create 2021/4/22 14:20
 */
@Data
@ToString
public class JobMonitorInfo {
    private String id;
    private String name;
    private String state;
    private String type;
    private String mode;
    private String webui;
}
