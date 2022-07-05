package com.github.superzhc.hadoop.es.index;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.util.ResponseUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author superz
 * @create 2022/2/10 11:18
 */
public class ESIndex extends ESCommon {
    public ESIndex(ESClient client) {
        super(client);
    }

    public String indices() {
        Response response = client.get(formatJson("/_cat/indices"));
        return ResponseUtils.getEntity(response);
    }

    public String create(String index) {
        // return create(index, null, null);
        Response response = client.put(String.format("/%s", index), null);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 创建索引
     *
     * @param index            索引名称
     * @param numberOfShards   每个索引的主分片数，若为空或小于1，则默认为5
     * @param numberOfReplicas 每个主分片的副本数，若为空或小于1，则默认为1
     * @return
     */
    public String create(String index, Integer numberOfShards, Integer numberOfReplicas) {
        if (null == numberOfShards || numberOfShards < 1) {
            numberOfShards = 5;
        }

        if (null == numberOfReplicas || numberOfReplicas < 1) {
            numberOfReplicas = 1;
        }

        String json = "{\"settings\":{\"number_of_shards\": " + numberOfShards + ",\"number_of_replicas\": " + numberOfReplicas + "}}";
        Response response = client.put(String.format("/%s", index), json);

        return ResponseUtils.getEntity(response);
    }

    public String get(String index) {
        String url = String.format("/%s", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    public String settings(String index) {
        String url = String.format("/%s/_settings", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    public String stats(String index) {
        String url = String.format("/%s/_stats", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引映射
     *
     * @param index
     * @return
     */
    public String mapping(String index) {
        String url = String.format("/%s/_mapping", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引的分片
     *
     * @param index
     * @return
     */
    public String shards(String index) {
        String url = String.format("/%s/_search_shards", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 获取索引的分段
     *
     * @param index
     * @return
     */
    public String segments(String index) {
        String url = String.format("/%s/_segments", index);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public String delete(String index) {
        String url = String.format("/%s", index);
        Response response = client.delete(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 根据查询清除索引的数据
     *
     * @param index
     * @param query
     * @return
     */
    public String deleteByQuery(String index, String query) {
        String url = String.format("/%s/_delete_by_query", index);
        Response response = client.post(url, query);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 清空索引的数据
     *
     * @param index
     * @return
     */
    public String clear(String index) {
        String query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).toString();
        return deleteByQuery(index, query);
    }
}
