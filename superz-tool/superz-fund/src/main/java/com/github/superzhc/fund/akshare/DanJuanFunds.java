package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.JSONUtils;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/20 14:01
 **/
public class DanJuanFunds {

    private static final String UA="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36";

    public static Table indexEva(){
        String url="https://danjuanfunds.com/djapi/index_eva/dj";
        String result=HttpRequest.get(url).body();
        JsonNode json= JsonUtils.json(result,"data","items");

        List<String> columnNames=JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows=JsonUtils.extractObjectData(json,columnNames);

        Table table= TableUtils.build(columnNames,dataRows);
        return table;
    }

    public static void main(String[] args) {
        String url = //"https://danjuanfunds.com/djapi/fund/nav/history/" + "501009"
                //"https://danjuanfunds.com/djapi/index_eva/dj"
                "http://www.csindex.com.cn/zh-CN/indices/index-detail/SH.000001"
//                        +
//                        "?earnings_performance="+
//                        "1%E4%B8%AA%E6%9C%88"+"&data_type=json"
                //
                ;

//        Map<String, Object> params = new HashMap<>();
//        params.put("earnings_performance", "1%E4%B8%AA%E6%9C%88");
//        params.put("data_type", "json");
//
//        String result = HttpRequest.get(url,params)
//                .header("X-Requested-With","XMLHttpRequest")
//                .userAgent(UA)
//                .acceptJson()
//                .body();
//        System.out.println(JSONUtils.format(result));

//        String str=HttpRequest.get("https://xueqiu.com").userAgent(UA).header("User-Agent");
//        System.out.println( str);

        //System.out.println(HttpRequest.get("https://xueqiu.com").userAgent(UA).headers());
        System.out.println("-------------------------");
        System.out.println(Arrays.asList(HttpRequest.get("https://xueqiu.com").userAgent(UA).cookies()));

        // Set-Cookie
        // xq_id_token
//        System.out.println(HttpRequest.get("https://xueqiu.com").userAgent(UA).parameter("Set-Cookie","xq_id_token"));

//        Table table=indexEva();
//        System.out.println(table.structure().printAll());
//        System.out.println(table.print());
//        System.out.println(table.shape());
    }
}
