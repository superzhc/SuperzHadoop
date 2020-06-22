package com.github.superzhc.es.search;

import org.apache.http.HttpHost;

import com.github.superzhc.es.ESClient;

/**
 * 对指定索引进行搜索
 * /_search：所有索引，所有type下的所有数据都搜索出来
 * /index1/_search：指定一个index，搜索其下所有type的数据
 * /index1,index2/_search：同时搜索两个index下的数据
 * /*1,*2/_search：按照通配符去匹配多个索引
 * 2020年04月22日 superz add
 */
public class ESIndexsSearch extends com.github.superzhc.es.ESSearch
{
    private String[] indices;

    public ESIndexsSearch(ESClient client, String... indices) {
        super(client);
        this.indices = indices;
    }

//    public ESIndexsSearch(HttpHost[] httpHosts, String... indices) {
//        super(httpHosts);
//        this.indices = indices;
//    }

    @Override
    protected String index() {
        if (null == indices || indices.length == 0)
            return "";
        return String.join(",", indices);
    }

    @Override protected String params(String url) {
        return url;
    }
}
