package com.github.superzhc.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.es.search.ESIndexsParamsSearch;
import com.github.superzhc.es.util.ResponseUtils;
import org.elasticsearch.client.Response;

/**
 * 2020年06月22日 superz add
 */
public class ESScroll extends ESCommon
{
    private static final String URL = "/_search/scroll";
    private String scrollId;

    public ESScroll(ESClient client) {
        super(client);
    }
    
    public ESScroll(ESClient client, String scrollId) {
        super(client);
        this.scrollId = scrollId;
    }

    public String getScrollId() {
        return scrollId;
    }

    public String query(String query, String scroll, String... indices) {
        // 在开始新的scroll查询开始之前，要删除老的
        clear();

        ESSearch search = new ESIndexsParamsSearch(client, "scroll=" + scroll, indices);
        String ret = search.queryDSL(query);
        scrollId = JSON.parseObject(ret).getString("_scroll_id");
        return ret;
    }

    public String get(String scroll) {
        JSONObject obj = new JSONObject();
        obj.put("scroll", scroll);// 通知Elasticsearch把搜索上下文再保持scroll时间
        obj.put("scroll_id", scrollId);
        Response response = client.post(URL, JSON.toJSONString(obj));
        return ResponseUtils.getEntity(response);
    }

    public String clear() {
        if (null != scrollId) {
            JSONObject obj = new JSONObject();
            obj.put("scroll_id", scrollId);
            Response response = client.delete(URL, JSON.toJSONString(obj));
            return ResponseUtils.getEntity(response);
        }
        return null;
    }
}
