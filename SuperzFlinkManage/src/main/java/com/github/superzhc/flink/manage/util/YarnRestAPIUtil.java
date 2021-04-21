package com.github.superzhc.flink.manage.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 官方文档地址：http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/ResourceManagerRest.html#Cluster_Application_State_API
 *
 * @author superz
 * @create 2021/4/15 17:43
 */
public class YarnRestAPIUtil {
    private static final Map<String, String> HEADERS;

    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("Accept", "application/json;charset=utf-8");
    }

    /**
     * 获取Yarn集群的信息
     * @param url
     * @return
     */
    public static String getClusterInfo(String url) {
        String requestUrl = String.format("%s/ws/v1/cluster/info");
        HttpResponse response = HttpRequest.get(requestUrl).addHeaders(HEADERS).execute();
        return response.body();
    }


}
