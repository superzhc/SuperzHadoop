package com.github.superzhc.fund.akshare;

import com.github.superzhc.common.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 参考：https://github.com/akfamily/akshare/blob/master/akshare/index/zh_stock_index_csindex.py
 *
 * @author superz
 * @create 2022/4/6 17:59
 **/
public class CSIndex {
    public static void main(String[] args) {
        String url = "https://www.csindex.com.cn/csindex-home/perf/index-perf";

        Map<String, String> params = new HashMap<>();
        params.put("indexCode", "H30374");
        // 时间必须要有
        params.put("startDate", "19900101");
        params.put("endDate", "20991231");

        String result = HttpRequest.get(url, params).body();
        System.out.println(result);
    }
}
