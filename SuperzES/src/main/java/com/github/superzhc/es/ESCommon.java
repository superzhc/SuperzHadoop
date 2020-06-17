package com.github.superzhc.es;

import org.apache.http.HttpHost;

/**
 * 2020年06月17日 superz add
 */
public abstract class ESCommon
{
    /**
     * type在6.0+以后被废弃了，一般统一设置成_doc，8.0+完全删除，此处实现可以统一控制
     */
    private final static String _type = "/_doc";

    protected ESClient client;

    public ESCommon(ESClient client) {
        this.client = client;
    }

    public ESCommon(HttpHost... httpHosts) {
        this.client = new ESClient(httpHosts);
    }

    public final String type() {
        return _type;
    }

    protected String formatJson(String url) {
        if (url.contains("format=json"))
            return url;

        if (url.contains("?"))
            url += "&format=json";
        else
            url += "&format=json";
        return url;
    }
}
