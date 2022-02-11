package com.github.superzhc.hadoop.es.search;

import com.github.superzhc.hadoop.es.ESClient;

/**
 * 2020年06月22日 superz add
 */
@Deprecated
public class ESIndexsParamsSearch extends ESIndexsSearch
{
    private String params;

    public ESIndexsParamsSearch(ESClient client, String params, String... indices) {
        super(client, indices);
        this.params = params;
    }

    @Override
    protected String params(String url) {
        if (url.contains("?"))
            url += "&";
        else
            url += "?";
        url += params;
        return url;
    }
}
