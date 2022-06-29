package com.github.superzhc.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.script.ScriptUtils;

import javax.script.ScriptEngine;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/28 10:35
 **/
public class ReportMain {
    public static void main(String[] args) {
        String url = "http://datainterface.eastmoney.com/EM_DataCenter/js.aspx";

        Map<String, Object> params = new HashMap<>();
        params.put("type", "SR");
        params.put("sty", "HYSR");
        params.put("mkt", "0");
        params.put("stat", "0");
        params.put("cmd", "4");
        params.put("code", "");
        params.put("sc", "");
        params.put("ps", 10);
        params.put("p", 1);
        params.put("js", "var KvxgytuM={\"data\":[(x)],\"pages\":\"(pc)\",\"update\":\"(ud)\",\"count\":\"(count)\"}");
        params.put("rt", "51811901");

        int page = 1;
        int size = 1;
        while (size == -1 || page <= size) {
            params.put("p", page);
            String result = HttpRequest.get(url, params).body();

            ScriptEngine engine = ScriptUtils.JSEngine();
            ScriptUtils.eval(engine, result);
            String value = ScriptUtils.string(engine, "KvxgytuM");
            JsonNode json = JsonUtils.json(value);
            String[] data = JsonUtils.array(json, "data");
            for (String item : data) {
                // System.out.println(item);
                // 无,2022/6/28 7:21:45,APPL9L9dJMWmIndustry,80092742,信达证券,3,464,,,石油加工行业大炼化周报：海外成品油价差继续扩大,石油行业,2.03
                String[] arr = item.split(",", -1);
                LocalDateTime dateTime = LocalDateTime.parse(arr[1], DateTimeFormatter.ofPattern("yyyy/M/d H:m:s"));
                String date = dateTime.format(DateTimeFormatter.BASIC_ISO_DATE);
                String company = arr[4];
                String title = arr[9];
                String industry = arr[10];




                // 该地址不可用，状态码为 302
                String url2 = String.format("http://data.eastmoney.com/report/%s/hy,%s.html", date, arr[2]);
                String html = HttpRequest.get(url2)/*.followRedirects(true)*/.body("gb2312");
                System.out.println(html);
                break;
            }

            // size = JsonUtils.integer(json, "pages");
            page++;
        }
    }
}
