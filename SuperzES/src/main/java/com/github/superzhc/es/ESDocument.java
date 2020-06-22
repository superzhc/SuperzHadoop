package com.github.superzhc.es;

import com.github.superzhc.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ESDocument extends ESCommon
{
    /* 文档所在的索引 */
    private String index;
    /* 文档的唯一标识 */
    private String id;

    public ESDocument(ESClient client, String index, String id) {
        super(client);
        this.index = index;
        this.id = id;
    }

//    public ESDocument(HttpHost[] httpHosts, String index, String id) {
//        super(httpHosts);
//        this.index = index;
//        this.id = id;
//    }

    private String document() {
        return String.format("/%s%s/%s", index, type(), id);
    }

    /**
     * 获取文档
     * @return
     */
    public String get() {
        Response response = client.get(document());
        return ResponseUtils.getEntity(response);
    }

    /**
     * 删除文档
     * @return
     */
    public String delete() {
        Response response = client.delete(document());
        return ResponseUtils.getEntity(response);
    }
}
