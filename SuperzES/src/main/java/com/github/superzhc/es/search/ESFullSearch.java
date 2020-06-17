package com.github.superzhc.es.search;

import com.github.superzhc.es.ESClient;
import org.apache.http.HttpHost;

/**
 * 2020年06月17日 superz add
 */
public class ESFullSearch extends AbstractESSearch
{
    public ESFullSearch(ESClient client) {
        super(client);
    }

    public ESFullSearch(HttpHost... httpHosts) {
        super(httpHosts);
    }

    @Override
    protected String index() {
        return "";
    }
}
