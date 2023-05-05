package com.github.superzhc.hadoop.es.sys;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.utils.ResponseUtils;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ESCluster
{
    private ESClient client;

    public ESCluster(ESClient client) {
        this.client = client;
    }

    public String info() {
        String url = "/";
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看集群的健康状况
     * @return
     */
    public String health() {
        Response response = client.get("/_cluster/health");
        return ResponseUtils.getEntity(response);
    }

    public String stats() {
        Response response = client.get("/_cluster/stats");
        return ResponseUtils.getEntity(response);
    }

    public String settings() {
        Response response = client.get("/_cluster/settings?include_defaults=true");
        return ResponseUtils.getEntity(response);
    }
}
