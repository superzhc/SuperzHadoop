package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.data.utils.XueQiuUtils.UA;

/**
 * @author superz
 * @create 2022/11/18 9:32
 **/
public class EastMoneyFund {
    public static List<Map<String,String>> fundRealNet(String... symbols) {
        if (null == symbols || symbols.length < 1) {
            throw new IllegalArgumentException("at least one fund");
        }

        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNFInfo";

        Map<String, Object> params = new HashMap<>();
        params.put("Fcodes", String.join(",", symbols));
        params.put("pageIndex", 1);
        params.put("pageSize", symbols.length);
        // params.put("Sort", "");
        // params.put("SortColumn", "");
        params.put("IsShowSE", false);
        // params.put("P", "F");
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.2.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        List<Map<String,String>> data= Arrays.asList(JsonUtils.objectArray2Map(json));

//        List<String[]> dataRows = JsonUtils.objectArrayWithKeys(json, columnNames);
//
//        Table table = TableUtils.build(dataRows);
//        table.column("FCODE").setName("code");
//        table.column("SHORTNAME").setName("name");
//        // 上一个交易日的值
//        table.column("PDATE").setName("latest_date");
//        table.column("NAV").setName("latest_net_worth");
//        table.column("ACCNAV").setName("latest_accumulated_net_worth");
//        table.column("NAVCHGRT").setName("latest_change");
//        // 预估值
//        table.column("GSZ").setName("estimate_net_worth");
//        table.column("GSZZL").setName("estimate_change");
//        table.column("GZTIME").setName("estimate_date");
        //table.column("").setName("");

        return data;
    }

}
