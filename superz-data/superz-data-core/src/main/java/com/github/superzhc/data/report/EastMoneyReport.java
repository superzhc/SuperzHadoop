package com.github.superzhc.data.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/19 22:41
 */
public class EastMoneyReport {
    public static List<Map<String, String>> reports() {
        return reports(LocalDate.now());
    }

    public static List<Map<String, String>> reports(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = date.format(formatter);
        return reports(dateStr);
    }

    public static List<Map<String, String>> reports(String dateStr) {
        String url = "http://reportapi.eastmoney.com/report/list?";

        Map<String, Object> params = new HashMap<>();
        //params.put("cb", "datatable8267652");
        params.put("industryCode", "*");
        params.put("pageSize", 1000);
        params.put("industry", "*");
        params.put("rating", "*");
        params.put("ratingChange", "*");
        params.put("beginTime", dateStr);
        params.put("endTime", dateStr);
        params.put("pageNo", 1);
        params.put("fields", "");
        params.put("qType", 1);
        params.put("orgCode", "");
        params.put("rcode", "");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");
        return Arrays.asList(JsonUtils.objectArray2Map(json));
    }

    public static void main(String[] args) {
        System.out.println(JsonUtils.asString(reports("2022-11-14")));
    }
}
