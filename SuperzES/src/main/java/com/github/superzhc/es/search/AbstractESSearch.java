package com.github.superzhc.es.search;

import com.github.superzhc.es.ESClient;
import com.github.superzhc.es.ESCommon;
import com.github.superzhc.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public abstract class AbstractESSearch extends ESCommon
{
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

    public String queryString(String query) {
        String url = String.format("%s/_search?q=%s", index(), query);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    public String queryDSL(String query) {
        String url = String.format("%s/_search", index());
        Response response = client.get(url, query);
        return ResponseUtils.getEntity(response);
    }

    public String queryAll() {
        String query = "{" + "\"query\": { \"match_all\": {} }" + "}";
        return queryDSL(query);
    }
}
