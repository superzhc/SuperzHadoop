package com.github.superzhc.es;

import com.github.superzhc.es.util.ResponseUtils;
import org.apache.http.HttpHost;
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

    public ESCluster(HttpHost... httpHosts) {
        this.client = new ESClient(httpHosts);
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
