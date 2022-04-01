package com.github.superzhc.hadoop.yarn.api;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.JSONUtils;

/**
 * 参考：
 * 1. https://hadoop.apache.org/docs/r3.2.2/hadoop-yarn/hadoop-yarn-site/WebServicesIntro.html [Hadoop 官网]
 * 2. https://developer.aliyun.com/article/840468
 *
 * @author superz
 * @create 2022/3/14 17:52
 **/
public class YarnRestApi {
    private String rmHttpAddress;
    private int port;
    private String version = "v1";

    public YarnRestApi(String rmHttpAddress, int port) {
        this.rmHttpAddress = rmHttpAddress;
        this.port = port;
    }

    /**
     * 获取单个应用相关信息的访问地址
     *
     * @param applicationId
     * @return
     */
    public String application(String applicationId) {
        return uri(String.format("/cluster/apps/%s", applicationId));
    }

    public String uri(String resourcepath) {
        if (resourcepath.trim().startsWith("/")) {
            resourcepath = resourcepath.substring(1);
        }

        return String.format("http://%s:%d/ws/%s/%s", rmHttpAddress, port, version, resourcepath);
    }

    public static void main(String[] args) {
        String host = "log-platform01";
        int port = 8088;

        YarnRestApi api = new YarnRestApi(host, port);
        String applicationId = "application_1648187566782_0014";

        String uri = null;
//        /* 集群信息，下面两个都可以 GET */
//        uri=api.uri("/cluster");
//        uri = api.uri("/cluster/info");
//
//        /* 集群指标 */
//        uri = api.uri("/cluster/metrics");
//
//        /* 集群调度 */
//        uri = api.uri("/cluster/scheduler");
//
//        /* 集群应用程序 */
//        uri = api.uri("cluster/apps");
//
//        /* 集群应用程序统计数据 */
//        uri = api.uri("/cluster/appstatistics");
//
//        /* 单个应用程序 Attempts */
//        uri = api.uri(String.format("/cluster/apps/%s/appattempts", "application_1644398887576_0057"));
//
//        uri = api.uri(String.format("/cluster/apps/%s/appattempts/%s/containers", "application_1644398887576_0057", "appattempt_1644398887576_0057_000001"));
//
//        /* 所有节点信息 */
//        uri = api.uri("/cluster/nodes");
//
//        /* 单个节点信息 */
//        uri = api.uri(String.format("/cluster/nodes/%s", "log-platform02:45540"));
//
        uri = api.application(applicationId);
        String result = HttpRequest.get(uri).body();
        System.out.println(JSONUtils.format(result));

    }
}
