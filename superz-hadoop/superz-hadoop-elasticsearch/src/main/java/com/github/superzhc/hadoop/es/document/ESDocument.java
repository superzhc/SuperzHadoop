package com.github.superzhc.hadoop.es.document;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.util.ResponseUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/2/10 11:39
 */
public class ESDocument extends ESCommon {
    private static final Logger logger = LoggerFactory.getLogger(ESDocument.class);

    public ESDocument(ESClient client) {
        super(client);
    }

    public String add(String index, String jsonData) {
        return add(index, null, jsonData);
    }

    /**
     * 新增文档
     *
     * @param index    索引名称
     * @param id       索引文档的唯一标识，不可重复，可不填但会自动生成索引的唯一标识
     * @param jsonData 索引数据
     * @return
     */
    public String add(String index, String id, String jsonData) {
        String url = String.format("/%s%s/%s", index, type(), null == id ? "" : id);
        Response response = client.post(url, jsonData);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 删除文档
     *
     * @param index 索引名称
     * @param id    索引文档的唯一标识，不可为空
     * @return
     */
    public String delete(String index, String id) {
        if (null == id) {
            return "删除文档的唯一标识不为null";
        }
        String url = String.format("/%s%s/%s", index, type(), id);
        Response response = client.delete(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 更新文档
     *
     * @param index    索引名称
     * @param id       索引文档的唯一标识
     * @param jsonData 更新的数据
     * @return
     */
    public String update(String index, String id, String jsonData) {
        String url = String.format("/%s%s/%s/_update", index, type(), id);
        Response response = client.post(url, jsonData);
        return ResponseUtils.getEntity(response);
    }

    public String upsert(String index, String id, String jsonData) {
        try {
            UpdateRequest ur = new UpdateRequest();
            ur.index(index).type(type().substring(1)).id(id)
                    .docAsUpsert(true)
                    .doc(jsonData, XContentType.JSON);
            if (logger.isDebugEnabled()) {
                logger.debug("id：" + id + (null == jsonData ? "" : ",请求体内容：" + jsonData));
            }
            UpdateResponse response = client.getHighLevelClient().update(ur, RequestOptions.DEFAULT);
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文档
     *
     * @param index
     * @param id
     * @return
     */
    public String get(String index, String id) {
        String url = String.format("/%s%s/%s", index, type(), id);
        Response response = client.get(url);
        return ResponseUtils.getEntity(response);
    }

    /**
     * 判断文档是否存在
     *
     * @param index
     * @param id
     * @return
     */
    public boolean exist(String index, String id) {
        String url = String.format("/%s%s/%s", index, type(), id);
        Response response = client.head(url);
        int code = response.getStatusLine().getStatusCode();
        return code == 200;
    }
}
