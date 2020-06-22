package com.github.superzhc.es.search;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.superzhc.es.ESClient;
import com.github.superzhc.es.ESCommon;
import com.github.superzhc.es.util.ResponseUtils;

/**
 * 2020年06月17日 superz add
 */
public abstract class AbstractESSearch extends ESCommon
{
    private static final Logger logger = LoggerFactory.getLogger(AbstractESSearch.class);

    public AbstractESSearch(ESClient client) {
        super(client);
    }

    public AbstractESSearch(HttpHost... httpHosts) {
        super(httpHosts);
    }

    /**
     * 无索引则是全文索引，反之则是查询特定索引
     * @return
     */
    protected abstract String index();

    /**
     * 推荐使用DSL方式，见#queryDSL
     * @param query
     * @return
     */
    @Deprecated
    public String queryString(String query) {
        String url = String.format("%s/_search?q=%s", index(), query);
        logger.debug("查询信息：{}", url);
        Response response = client.get(url);
        logger.debug("响应信息：{}", response.toString());
        return ResponseUtils.getEntity(response);
    }

    public String queryDSL(String query) {
        String url = String.format("%s/_search", index());
        logger.debug("查询的Url:{}\n查询的条件：{}", url, query);
        Response response = client.get(url, query);
        logger.debug("响应信息：{}", response.toString());
        return ResponseUtils.getEntity(response);
    }

    public String queryAll() {
        // String query = "{\"query\": { \"match_all\": {} }}";
        // 使用Elasticsearch的high level构建查询
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        String query = builder.toString();
        return queryDSL(query);
    }
}
