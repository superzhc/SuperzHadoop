package com.github.superzhc.flink.manage.job.monitor.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.flink.manage.config.BigDataConfig;
import com.github.superzhc.flink.manage.job.monitor.JobMonitor;
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
@Component("yarn")
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
    public String status(String applicationId) {
        String path = String.format("%s/ws/v1/cluster/apps/%s/state", config.getYarnRmHttpAddress(), applicationId);
        log.debug("monitor url:{}", path);
        String response = HttpRequest.get(path)
                .addHeaders(HEADERS)
                .execute()
                .body();
        JSONObject responseObj = JSON.parseObject(response);
        return responseObj.getString("state");
    }
}
