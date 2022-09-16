package com.github.superzhc.data.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.script.ScriptUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/14 9:16
 **/
public class SinaStock {
    public static List<Map<String, String>> stocks() {
        // 获取总数
        String countUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCount?node=hs_a";
        String cResult = HttpRequest.get(countUrl).body();
        String total = cResult.substring(1, cResult.length() - 1);

        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData";

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        // 直接一次性将所有数据给读取出来
        params.put("num", /*"80"*/total);
        params.put("sort", "symbol");
        params.put("asc", "1");
        params.put("node", "hs_a");
        params.put("_s_r_a", "page");

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result);
        List<Map<String, String>> dataRows = Arrays.asList(JsonUtils.objectArray2Map(json));

        return dataRows;
    }

    public static List<Map<String, Object>> history(String symbol) {
        String url = String.format("https://finance.sina.com.cn/realstock/company/%s/hisdata/klc_kl.js", symbol);

        String result = HttpRequest.get(url).body();
        String text = result.split("=")[1].split(";")[0].replace("\"", "");

        ScriptEngine engine = ScriptUtils.JSEngine();
        ScriptUtils.load(engine, SinaStock.class.getClassLoader().getResource("js/sina/hk_js_decode.js").getPath());
        ScriptObjectMirror decodeText = ScriptUtils.call(engine, "d", text);

        List lst = ScriptUtils.array(decodeText);

        return lst;
    }

    public static void main(String[] args) {
        // stocks();
//         history("sh000001");
    }
}
