package com.github.superzhc.flink.manage.job.monitor.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author superz
 * @create 2021/4/22 9:52
 */
@Data
@ToString
public class YarnJobMonitorInfo {
    private String id;
    private String user;
    private String name;
    private String queue;
    /**
     * The application state according to the ResourceManager - valid values are members of the YarnApplicationState enum: NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED
     */
    private String state;
    /**
     * The final status of the application if finished - reported by the application itself - valid values are the members of the FinalApplicationStatus enum: UNDEFINED, SUCCEEDED, FAILED, KILLED
     */
    private String finalStatus;
    private Double progress;
    private String trackingUI;
    private String trackingUrl;
    private String applicationType;
    private String applicationTags;
    private Integer priority;
    private Long startedTime;
    private Long launchTime;
    private Long finishedTime;
}
