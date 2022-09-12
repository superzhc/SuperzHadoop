package com.github.superzhc.financial.data.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/8/26 14:51
 */
public class HaoHaoZhu {
    public static Table wholeHouse(String keyword) {
        String url = "https://www.haohaozhu.cn/f/y/api/Share/GetArticle";

        Map<String, Object> form = new HashMap<>();
        form.put("keyword", keyword);
        form.put("page", 1);

        String result = HttpRequest.post(url).form(form).body();
        JsonNode data = JsonUtils.json(result, "data", "rows");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "article_info", "title"));
            dataRow.put("tags", JsonUtils.string(item, "article_info", "admin_tag"));
            dataRow.put("area", JsonUtils.string(item, "article_info", "area_name"));
            dataRow.put("content", JsonUtils.string(item, "article_info", "description"));
            dataRow.put("image", JsonUtils.string(item, "article_info", "cover_pic_url"));
            dataRow.put("image2", JsonUtils.string(item, "article_info", "cover_pic_url_square"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table discover(String keyword) {
        String url = "https://www.haohaozhu.cn/f/y/api/Share/AllPhotoInPc";

        Map<String, Object> forms = new HashMap<>();
        forms.put("keyword", null == keyword ? "" : keyword);
        forms.put("page", 1);
        forms.put("time", System.currentTimeMillis());

        String result = HttpRequest.post(url).form(forms).body();
        JsonNode data = JsonUtils.json(result, "data", "rows");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title",JsonUtils.string(item,"photo","photo_info","title"));
            dataRow.put("remark",JsonUtils.string(item,"photo","photo_info","remark"));
            dataRow.put("tags",JsonUtils.string(item,"photo","photo_info","admin_tag"));
            dataRow.put("score",JsonUtils.string(item,"photo","photo_info","admin_score"));
            dataRow.put("image",JsonUtils.string(item,"photo","photo_info","pic_url"));
            dataRow.put("link",JsonUtils.string(item,"photo","photo_info","share_url"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        String keyword = "";

        discover(keyword);
    }
}
