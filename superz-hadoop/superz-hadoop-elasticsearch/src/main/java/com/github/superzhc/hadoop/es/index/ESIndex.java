package com.github.superzhc.hadoop.es.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.utils.ResponseUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/2/10 11:18
 */
public class ESIndex extends ESCommon {
    public ESIndex(ESClient client) {
        super(client);
    }

    public List<String> indices() {
        Response response = client.get(formatJson("/_cat/indices"));
        /*
        返回结果：
        [
            {
                "health": "green",
                "status": "open",
                "index": "ui_template",
                "uuid": "se83R0ObS6G_bM-L1Udo-A",
                "pri": "1",
                "rep": "1",
                "docs.count": "8",
                "docs.deleted": "0",
                "store.size": "158.5kb",
                "pri.store.size": "79.2kb"
            },
            ...
        ]
        */
        String result = ResponseUtils.getEntity(response);
        JsonNode json = JsonUtils.json(result);

        String[] indices = JsonUtils.objectOneArray(json, "index");
        return Arrays.asList(indices);
    }

    public boolean exist(String index) {
        Response response = client.head(String.format("/%s", index));
        return response.getStatusLine().getStatusCode() == 200;
    }

    public String create(String index) {
        return create(index, null);
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
        return create(index, numberOfShards, numberOfReplicas, (String) null);
    }

    public String create(String index, Integer numberOfShards, Integer numberOfReplicas, Map<String, String> mappings) {
        ObjectNode mappingsNode = JsonUtils.mapper().createObjectNode();
        if (null != mappings && mappings.size() > 0) {
            ObjectNode properties = JsonUtils.mapper().createObjectNode();
            for (Map.Entry<String, String> mapping : mappings.entrySet()) {
                ObjectNode property = JsonUtils.mapper().createObjectNode();
                property.put("type", mapping.getValue());
                properties.set(mapping.getKey(), property);
            }
            mappingsNode.set("properties", properties);
        }
        return create(index, numberOfShards, numberOfReplicas, JsonUtils.asString(mappingsNode));
    }

    public String create(String index, Integer numberOfShards, Integer numberOfReplicas, String mappingsJson) {
        ObjectNode json = JsonUtils.mapper().createObjectNode();

        if (null == numberOfShards || numberOfShards < 1) {
            numberOfShards = 5;
        }

        if (null == numberOfReplicas || numberOfReplicas < 1) {
            numberOfReplicas = 1;
        }

        ObjectNode settings = JsonUtils.mapper().createObjectNode();
        settings.put("number_of_shards", numberOfShards);
        settings.put("number_of_replicas", numberOfReplicas);
        json.set("settings", settings);

        if (null != mappingsJson && mappingsJson.trim().length() > 0) {
            json.set("mappings", JsonUtils.json(mappingsJson));
        }

        return create(index, JsonUtils.asString(json));
    }

    public String create(String index, String json) {
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
     * 获取索引映射，支持匹配模式，返回的结果会将所有匹配索引的映射展示出来
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
