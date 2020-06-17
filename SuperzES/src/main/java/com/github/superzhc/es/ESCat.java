package com.github.superzhc.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

import com.github.superzhc.es.util.ResponseUtils;

/**
 * 2020年04月21日 superz add
 */
public class ESCat extends ESCommon
{
    public ESCat(ESClient client) {
        super(client);
    }

    public ESCat(HttpHost httpHost) {
        super(httpHost);
    }

    public String allocation() {
        Response response = client.get(formatJson("/_cat/allocation"));
        return ResponseUtils.getEntity(response);
    }

    public String count() {
        Response response = client.get(formatJson("/_cat/count"));
        return ResponseUtils.getEntity(response);
    }

    public String fielddata() {
        Response response = client.get(formatJson("/_cat/fielddata"));
        return ResponseUtils.getEntity(response);
    }

    public String health() {
        Response response = client.get("/_cat/health");
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看索引信息
     * @return
     */
    public String indices() {
        Response response = client.get(formatJson("/_cat/indices"));
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看主节点信息
     * @return
     */
    public String master() {
        Response response = client.get(formatJson("/_cat/master"));
        return ResponseUtils.getEntity(response);
    }

    public String nodeAttrs() {
        Response response = client.get(formatJson("/_cat/nodeattrs"));
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看节点信息
     * @return
     */
    public String nodes() {
        Response response = client.get(formatJson("/_cat/nodes?full_id=true"));
        return ResponseUtils.getEntity(response);
    }

    public String pendingTasks() {
        Response response = client.get(formatJson("/_cat/pending_tasks"));
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看插件信息
     * @return
     */
    public String plugins() {
        Response response = client.get("/_cat/plugins");
        return ResponseUtils.getEntity(response);
    }

    public String recovery() {
        Response response = client.get("/_cat/recovery");
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看分段信息
     * @return
     */
    public String segments() {
        Response response = client.get(formatJson("/_cat/segments"));
        return ResponseUtils.getEntity(response);
    }

    /**
     * 查看分片信息
     * @return
     */
    public String shards() {
        Response response = client.get(formatJson("/_cat/shards"));
        return ResponseUtils.getEntity(response);
    }

    public String threadPool() {
        Response response = client.get("/_cat/thread_pool");
        return ResponseUtils.getEntity(response);
    }

    /**
     * _cat 帮助文档
     * @return
     */
    public String help() {
        Response response = client.get("/_cat");
        return ResponseUtils.getEntity(response);
    }
}
