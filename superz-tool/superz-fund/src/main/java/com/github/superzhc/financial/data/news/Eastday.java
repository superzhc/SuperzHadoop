package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/18 13:44
 **/
public class Eastday {
    public static Table shehui() {
        return news24("shehui");
    }

    public static Table yule() {
        return news24("yule");
    }

    public static Table guoji() {
        return news24("guoji");
    }

    public static Table junshi() {
        return news24("junshi");
    }

    public static Table yangsheng() {
        return news24("yangsheng");
    }

    public static Table qiche() {
        return news24("qiche");
    }

    public static Table tiyu() {
        return news24("tiyu");
    }

    public static Table caijing() {
        return news24("caijing");
    }

    public static Table youxi() {
        return news24("youxi");
    }

    public static Table keji() {
        return news24("keji");
    }

    public static Table guonei() {
        return news24("guonei");
    }

    public static Table chongwu() {
        return news24("chongwu");
    }

    public static Table qinggan() {
        return news24("qinggan");
    }

    public static Table renwen() {
        return news24("renwen");
    }

    public static Table jiaoyu() {
        return news24("jiaoyu");
    }

    private static Table news24(String category) {
        String rootUrl = "https://mini.eastday.com";
        String url = String.format("%s/ns/api/detail/trust/trust-news-%s.json", rootUrl, category);

        String result = HttpRequest.get(url).body();
        Pattern pattern = Pattern.compile("\\((.*)\\)");
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            result = matcher.group(1);
        }

        JsonNode data = JsonUtils.json(result, "data", "trust");

        List<Map<String, Object>> dataRows = new ArrayList<>(data.size());
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "topic"));
            dataRow.put("source", JsonUtils.string(item, "source"));
            dataRow.put("link", String.format("%s%s", rootUrl, JsonUtils.string(item, "url")));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table hotSearch() {
        String url = "https://apin.eastday.com/api/news/Find";

        Map<String, Object> data = new HashMap<>();
        data.put("currentPage", 1);
        data.put("newsType", "1");
        data.put("pageSize", 30);
        data.put("query", "");

        String result = HttpRequest.post(url).userAgent(UA_CHROME).json(data).body();
        JsonNode json = JsonUtils.json(result, "newsList", "list");

        List<Map<String, Object>> dataRows = new ArrayList<>(json.size());
        for (JsonNode item : json) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("brief", JsonUtils.string(item, "abstracts"));
            dataRow.put("link", JsonUtils.string(item, "url"));
            dataRow.put("source", JsonUtils.string(item, "infoSource"));
            dataRow.put("pubDate", JsonUtils.string(item, "time"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table original() {
        String url = "https://apin.eastday.com/api/news/Portrait";

        Map<String, Object> data = new HashMap<>();
        data.put("currentPage", 1);
        data.put("pageSize", 30);

        String result = HttpRequest.post(url).userAgent(UA_CHROME).json(data).body();
        JsonNode json=JsonUtils.json(result,"list");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for(JsonNode item:json){
            Map<String,Object> dataRow=new LinkedHashMap<>();
            dataRow.put("title",JsonUtils.string(item,"title"));
            dataRow.put("link",JsonUtils.string(item,"url"));
            dataRow.put("source",JsonUtils.string(item,"infoSource"));
            dataRow.put("pubDate",JsonUtils.string(item,"time"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = original();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
