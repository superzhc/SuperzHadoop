package com.github.superzhc.es;

import com.github.superzhc.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ESDocument extends ESCommon
{
    public ESDocument(ESClient client) {
        super(client);
    }

    public ESDocument(HttpHost... httpHosts) {
        super(httpHosts);
    }

    /**
     * 获取文档
     * @param index
     * @param id
     * @return
     */
    public String get(String index, String id) {
        String url = String.format("/%s%s/%s", index, type(), id);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }
}
