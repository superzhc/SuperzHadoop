package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/4/27 0:45
 */
public class JiSiLuFund {
    public static Table etf() {
        String url = "https://www.jisilu.cn/data/etf/etf_list/?___jsl=LST___t=1650991707906&volume=&unit_total=&rp=25";

        Map<String, Object> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%d", System.currentTimeMillis()));
        params.put("volume", "");
        params.put("unit_total", "");
        params.put("rp", 25);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = Arrays.asList(
                "fund_id",
                "fund_nm",
                "price",//现价
                "volume",//成交额（万元）
                "increase_rt",//涨幅
                "estimate_value",//估值
                "last_est_time",//估值时间
                "index_id",
                "index_nm",
                "index_increase_rt",//指数涨幅
                "idx_price_dt",
                "fee",//托管费(%)
                // "m_fee",
                // "t_fee",
                // "urls",//基金公司官网地址
                // "eval_flg",
                // "ex_dt",
                // "ex_info",
                "creation_unit",//最小申赎（万份）
                "amount",//份额（万份）
                // "amount_notes",
                "unit_total",//规模（亿元）
                "unit_incr",//规模变化（亿元）
                "last_dt",
                "last_time",
                "discount_rt",//溢价率
                "fund_nav",//净值
                "nav_dt",//净值日期
                "issuer_nm",//基金公司
                "owned",
                "holded",
                "pe",
                "pb"
        );
        int columnNamesSize = columnNames.size();

        List<String[]> dataRows = new ArrayList<>();
        for (int i = 0, len = json.size(); i < len; i++) {
            String[] row = new String[columnNamesSize];
            JsonNode node = json.get(i).get("cell");
            for (int j = 0; j < columnNamesSize; j++) {
                String columnName = columnNames.get(j);
                JsonNode item = node.get(columnName);
                row[j] = null == item ? null : item.asText();
            }
            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static void main(String[] args) {
        Table table = etf();
        System.out.println(table.printAll());
    }
}
