package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/13 15:17
 **/
public class CailianShe {
    public static Table telegraph() {
        String url = "https://www.cls.cn/nodeapi/updateTelegraphList";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("app", "CailianpressWeb");
        params.put("category", "");
        params.put("hasFirstVipArticle", "1");
//        params.put("lastTime",);
        params.put("os", "web");
        params.put("rn", 100);//数量
        params.put("subscribedColumnIds", "");
        params.put("sv", "7.7.5");
        // params.put("sign","b2c3f844c6867e28951c61c8e889de36");

        String result = HttpRequest.get(url, params)
                .header("Host", "www.cls.cn")
                .userAgent(UA_CHROME)
                // .followRedirects(true)
                // .cookies("HWWAFSESID=82477a3143c62f598f; HWWAFSESTIME=1660374917550")
                .body();
        JsonNode json = JsonUtils.json(result, "data", "roll_data");

        List<String> columnNames = Arrays.asList("id", "ctime", "title", /*"type",*/ "brief", "content", "shareurl", "subjects");
        List<String[]> data = JsonUtils.objectArrayWithKeys(json, columnNames);
        Table table = TableUtils.build(data);
        return table;
    }

    public static void main(String[] args) {
        Table table = telegraph();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
