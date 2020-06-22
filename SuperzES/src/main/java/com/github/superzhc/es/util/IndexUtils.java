package com.github.superzhc.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.es.ESCat;
import com.github.superzhc.es.ESClient;
import org.elasticsearch.client.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020年06月22日 superz add
 */
public class IndexUtils
{
    /**
     * 获取所有索引
     * @return
     */
    public static List<String> indices(ESClient client) {
        ESCat cat = new ESCat(client);
        String response = cat.indices();
        JSONArray arr = JSON.parseArray(response);

        List<String> lst = new ArrayList<>(arr.size());
        for (int i = 0, len = arr.size(); i < len; i++) {
            JSONObject obj = arr.getJSONObject(i);
            lst.add(obj.getString("index"));
        }
        return lst;
    }
}
