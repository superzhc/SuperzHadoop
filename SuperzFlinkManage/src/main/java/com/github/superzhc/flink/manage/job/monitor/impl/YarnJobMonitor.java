package com.github.superzhc.flink.manage.job.monitor.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.flink.manage.config.BigDataConfig;
import com.github.superzhc.flink.manage.job.monitor.JobMonitor;
import com.github.superzhc.flink.manage.job.monitor.JobMonitorConstant;
import com.github.superzhc.flink.manage.job.monitor.entity.JobMonitorInfo;
import com.github.superzhc.flink.manage.job.monitor.entity.YarnJobMonitorInfo;
import com.github.superzhc.flink.manage.util.YarnRestAPIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/12 19:14
 */
@Slf4j
@Component(JobMonitorConstant.MODE_YARN)
public class YarnJobMonitor implements JobMonitor {
    private static final Map<String, String> HEADERS;

    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("Accept", "application/json, charset=utf8");
    }

    @Autowired
    private BigDataConfig config;

    @Override
    public JobMonitorInfo info(String applicationId) {
        String response = YarnRestAPIUtil.getApplicationInfo(config.getYarnRmHttpAddress(), applicationId);
        if (StrUtil.isBlank(response)) {
            return null;
        }

        JSONObject respObj = JSON.parseObject(response);
        YarnJobMonitorInfo yarnJobMonitorInfo = respObj.getObject("app", YarnJobMonitorInfo.class);
        JobMonitorInfo jobMonitorInfo = new JobMonitorInfo();
        jobMonitorInfo.setId(yarnJobMonitorInfo.getId());
        jobMonitorInfo.setName(yarnJobMonitorInfo.getName());
        jobMonitorInfo.setMode(JobMonitorConstant.MODE_YARN);
        // 状态，如果任务的状态是已完成，获取任务自身运行的状态作为任务的最终状态
        if ("FINISHED".equals(yarnJobMonitorInfo.getState())) {
            jobMonitorInfo.setState(yarnJobMonitorInfo.getFinalStatus());
        } else {
            jobMonitorInfo.setState(yarnJobMonitorInfo.getState());
        }
        // 任务类型
        if (yarnJobMonitorInfo.getApplicationType().toLowerCase().contains(JobMonitorConstant.TYPE_FLINK)) {
            jobMonitorInfo.setType(JobMonitorConstant.TYPE_FLINK);
        }
        jobMonitorInfo.setWebui(yarnJobMonitorInfo.getTrackingUrl());
        return jobMonitorInfo;
    }

    @Override
    public String status(String applicationId) {
        String response = YarnRestAPIUtil.getApplicationState(config.getYarnRmHttpAddress(), applicationId);
        if (StrUtil.isBlank(response)) {
            return null;
        }

        JSONObject responseObj = JSON.parseObject(response);
        return responseObj.getString("state");
    }
}
