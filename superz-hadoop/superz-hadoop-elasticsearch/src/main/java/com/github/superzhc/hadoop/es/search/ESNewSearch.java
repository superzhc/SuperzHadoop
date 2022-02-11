package com.github.superzhc.hadoop.es.search;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.util.ResponseUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author superz
 * @create 2022/2/11 11:30
 */
public class ESNewSearch extends ESCommon {
    public ESNewSearch(ESClient client) {
        super(client);
    }

    /**
     * 推荐使用DSL方式，见#queryDSL
     *
     * @param query
     * @return
     */
    public String queryString(String query, String... indexes) {
        String indeics;
        if (null == indexes || indexes.length == 0) {
            indeics = "";
        } else {
            indeics = String.format("/%s", String.join(",", indexes));
        }
        String url = String.format("%s/_search?q=%s", indexes, query);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 对指定索引进行搜索
     * /_search：所有索引，所有type下的所有数据都搜索出来
     * /index1/_search：指定一个index，搜索其下所有type的数据
     * /index1,index2/_search：同时搜索两个index下的数据
     * /*1,*2/_search：按照通配符去匹配多个索引
     * @param query
     * @param indexes
     * @return
     */
    public String queryDSL(String query, String... indexes) {
        String indeics;
        if (null == indexes || indexes.length == 0) {
            indeics = "";
        } else {
            indeics = String.format("/%s", String.join(",", indexes));
        }
        String url = String.format("%s/_search", indeics);
        Response response = client.get(url, query);
        return ResponseUtils.getEntity(response);
    }

    public String queryAll(String... indexes) {
        // String query = "{\"query\": { \"match_all\": {} }}";
        // 使用Elasticsearch的high level构建查询
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        String query = builder.toString();
        return queryDSL(query, indexes);
    }
}
