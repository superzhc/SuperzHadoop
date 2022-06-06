package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.HttpConstant;
import com.github.superzhc.common.XueQiuUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.fund.utils.IndexConstant;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据来源：
 * 1. https://xueqiu.com/S/CSI000905?from=status_stock_match
 *
 * @author superz
 * @create 2022/5/12 1:27
 */
public class XueQiuIndex {
    public static Table realTime(String symbol) {
        String url = "https://stock.xueqiu.com/v5/stock/quote.json";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", transform(symbol));
        params.put("extend", "detail");

        String result = HttpRequest.get(url, params).userAgent(HttpConstant.UA).cookies(XueQiuUtils.cookies()).body();
        JsonNode json = JsonUtils.json(result, "data");

        Map<String, Object> map = new LinkedHashMap<>();
        // 当前状态
        String status = json.get("market").get("status").asText();
        map.put("status", status);

        JsonNode quote = json.get("quote");
        // 指数代码
        map.put(IndexConstant.INDEX_CODE, quote.get("code").asText());
        // 指数名称
        map.put(IndexConstant.INDEX_NAME, quote.get("name").asText());
        // 昨收
        map.put(IndexConstant.INDEX_TRADE_LAST_CLOSE, quote.get("last_close").asDouble());
        // 今开
        map.put(IndexConstant.INDEX_TRADE_OPEN, quote.get("open").asDouble());
        // 当前点数
        map.put("current", quote.get("current").asDouble());
        // 最高
        map.put(IndexConstant.INDEX_TRADE_HIGH, quote.get("high").asDouble());
        // 最低
        map.put(IndexConstant.INDEX_TRADE_LOW, quote.get("low").asDouble());
        // 成交额
        map.put("amount", quote.get("amount").asLong());
        // 成交量
        map.put(IndexConstant.INDEX_TRADE_VOLUME, quote.get("volume").asLong());
        // 振幅（单位百分比）
        map.put("amplitude", quote.get("amplitude").asDouble());
        // 涨跌幅（单位百分比）
        map.put("quote_change", quote.get("percent").asDouble());
        // 涨跌额
        map.put("quote_change_amount", quote.get("chg").asDouble());
        map.put("turnover_rate", quote.get("turnover_rate").asDouble());
        // map.put("",quote.get(""));

        // 平均点数
        // avg_price: 5737.8173
        // currency: "CNY"
        // current_year_percent: -22.03
        // delayed: 0
        // exchange: "CSI"
        // float_market_capital: null
        // float_shares: null

        // 52周最高
        // high52w: 7688.6036
        // issue_date: null
        // lock_set: 0
        // lot_size: 100
        // 52周最低
        // low52w: 4151.414
        // market_capital: null
        // status: 1
        // sub_type: ""
        // symbol: "CSI000905"
        // tick_size: 0.01
        // time: 1652257798000
        // timestamp: 1652257798000
        // total_shares: null
        // type: 26

        Table table = TableUtils.map2Table(map);
        return table;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return String.format("%s%s", "CSI", ss[0]);
    }

    public static void main(String[] args) {
        String symbol = "000905.CSI";

        Table table = realTime(symbol);

        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());
    }
}
