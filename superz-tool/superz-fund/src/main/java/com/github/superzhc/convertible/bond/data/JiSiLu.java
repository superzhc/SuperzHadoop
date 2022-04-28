package com.github.superzhc.convertible.bond.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.common.HttpConstant;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlReadOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/27 19:10
 **/
public class JiSiLu {

    private static final Logger log = LoggerFactory.getLogger(JiSiLu.class);

    public static Table convertibleBond() {
        return convertibleBond(null);
    }

    public static Table convertibleBond(String cookie) {
        String url = "https://app.jisilu.cn/data/cbnew/cb_list_new/";

        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", HttpConstant.UA);
        // 需要cookie，不然只显示一部分数据
        if (null != cookie && cookie.trim().length() > 0) {
            headers.put("Cookie", cookie);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%d", System.currentTimeMillis()));


        Map<String, Object> json = new HashMap<>();
        json.put("fprice", "");
        json.put("tprice", "");
        json.put("curr_iss_amt", "");
        json.put("volume", "");
        json.put("svolume", "");
        json.put("premium_rt", "");
        json.put("ytm_rt", "");
        json.put("market", "");
        json.put("rating_cd", "");
        json.put("is_search", "N");
        json.put("market_cd", new String[]{"shmb", "shkc", "szmb", "szcy"});
        json.put("btype", "");
        json.put("listed", "Y");
        json.put("qflag", "N");
        json.put("sw_cd", "");
        json.put("bond_ids", "");
        json.put("rp", "50");

        String result = HttpRequest.post(url, params).headers(headers).json(json).body();
        JsonNode node = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(node, "cell");
        List<String[]> dataRows = JsonUtils.extractObjectData(node, columnNames, "cell");
        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondToIssue() {
        String url = String.format("https://www.jisilu.cn/data/cbnew/pre_list/");

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("progress", "");
        form.put("rp", 22);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondRedeem(){
        String url="https://www.jisilu.cn/data/cbnew/redeem_list/";

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("page", 1);
        form.put("rp", 50);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondBack(){
        String url="https://www.jisilu.cn/data/cbnew/huishou_list/";

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("page", 1);
        form.put("rp", 50);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondAdjustment(String symbol) {
        String url = String.format("https://www.jisilu.cn/data/cbnew/adj_logs/?bond_id=%s", symbol);
        String result = HttpRequest.get(url).userAgent(HttpConstant.UA).body();

        Table table;
        try {
            /**
             * 返回值：
             * 1. 该可转债没有转股价调整记录，服务端返回文本 '暂无数据'
             * 2. 无效可转债代码，服务端返回 {"timestamp":1639565628,"isError":1,"msg":"无效代码格式"}
             * 以上两种情况，返回空的 DataFrame
             */
            if (!result.contains("</table>")) {
                table = Table.create();
            } else {
                HtmlReadOptions options = HtmlReadOptions.builderFromString(result).tableIndex(0).build();
                table = Table.read().usingOptions(options);
            }
        } catch (Exception e) {
            log.error("解析失败", e);
            table = Table.create();
        }
        return table;
    }

    public static void main(String[] args) {
        Table table = convertibleBondAdjustment("110059");

        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());
    }
}
