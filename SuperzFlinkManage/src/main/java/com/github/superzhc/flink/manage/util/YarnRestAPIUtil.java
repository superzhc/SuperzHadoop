package com.github.superzhc.flink.manage.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 官方文档地址：http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/ResourceManagerRest.html#Cluster_Application_State_API
 *
 * @author superz
 * @create 2021/4/15 17:43
 */
@Slf4j
public class YarnRestAPIUtil {
    private static final Map<String, String> HEADERS;

    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("Accept", "application/json;charset=utf-8");
    }

    /**
     * 获取集群的信息
     *
     * @param url
     * @return
     */
    public static String getClusterInfo(String url) {
        String requestUrl = String.format("%s/ws/v1/cluster/info", url);
        return execute(requestUrl);
    }

    /**
     * 获取集群的相关指标
     *
     * @param url
     * @return
     */
    public static String getClusterMetrics(String url) {
        String requestUrl = String.format("%s/ws/v1/cluster/metrics", url);
        return execute(requestUrl);
    }

    /**
     * 获取应用信息
     *
     * @param url
     * @param applicationId
     * @return
     */
    public static String getApplicationInfo(String url, String applicationId) {
        String requestUrl = String.format("%s/ws/v1/cluster/apps/%s", url, applicationId);
        return execute(requestUrl);
    }

    /**
     * 获取应用状态
     *
     * @param url
     * @param applicationId
     * @return
     */
    public static String getApplicationState(String url, String applicationId) {
        String requestUrl = String.format("%s/ws/v1/cluster/apps/%s/state", url, applicationId);
        return execute(requestUrl);
    }

    private static String execute(String url) {
        log.debug("yarn url:{}", url);
        HttpResponse response = HttpRequest.get(url).addHeaders(HEADERS).execute();
        if (response.isOk()) {
            return response.body();
        } else {
            log.error("yarn error:{}", response.body());
            return null;
        }
    }
}
