package com.github.superzhc.es;

import com.github.superzhc.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ESIndex extends ESCommon
{
    private String index;

    public ESIndex(ESClient client, String index) {
        super(client);
        this.index = index;
    }

    public ESIndex(HttpHost httpHost, String index) {
        super(httpHost);
        this.index = index;
    }

    public String stats() {
        String url = String.format("/%s/_stats", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引映射
     * @return
     */
    public String mapping() {
        String url = String.format("/%s/_mapping", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引的分片
     * @return
     */
    public String shards() {
        String url = String.format("/%s/_search_shards", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引的分段
     * @return
     */
    public String segments() {
        String url = String.format("/%s/_segments", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 删除索引
     * @return
     */
    public String delete() {
        String url = String.format("/%s", index);
        Response response = client.delete(url);
        return ResponseUtils.getEntity(response);
    }
}
