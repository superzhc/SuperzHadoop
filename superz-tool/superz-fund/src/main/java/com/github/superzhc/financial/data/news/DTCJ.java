package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/17 14:01
 **/
public class DTCJ {
    public static Table dataHero() {
        return dataHero("");
    }

    /**
     * 5: '侠创',
     * 6: '纽约数据科学学院',
     * 9: 'RS实验所',
     * 10: '阿里云天池'
     *
     * @param topic
     * @return
     */
    public static Table dataHero(String topic) {
        String url = "https://dtcj.com/api/v1/data_hero_informations";

        Map<String, Object> params = new HashMap<>();
        params.put("per", 15);
        params.put("page", 1);
        params.put("topic_id", topic);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>(data.size());

        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("author", JsonUtils.string(item, "author"));
            dataRow.put("link", String.format("https://dtcj.com/topic/%s", JsonUtils.string(item, "id")));
            dataRow.put("pubDate", JsonUtils.aLong(item, "date"));
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = dataHero("10");
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
