package com.github.superzhc.financial.data.report;

import com.github.superzhc.common.http.HttpRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/28 11:36
 **/
public class EastMoneyReport {
    public static void main(String[] args) {
        String url = "http://reportapi.eastmoney.com/report/list?";

        Map<String, Object> params = new HashMap<>();
        //params.put("cb", "datatable8267652");
        params.put("industryCode", "*");
        params.put("pageSize", 10);
        params.put("industry", "*");
        params.put("rating", "*");
        params.put("ratingChange", "*");
        params.put("beginTime", "2018-11-20");
        params.put("endTime", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        params.put("pageNo", 1);
        params.put("fields", "");
        params.put("qType", 1);
        params.put("orgCode", "");
        params.put("rcode", "");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        System.out.println(result);
    }
}
