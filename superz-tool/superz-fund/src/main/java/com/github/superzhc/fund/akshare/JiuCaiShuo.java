package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/7 11:40
 **/
public class JiuCaiShuo {

    public static Table indexValuation() {
        String url = "https://api.jiucaishuo.com/v2/guzhi/showcategory";

        Map<String, Object> params = new HashMap<>();
        params.put("act_time", System.currentTimeMillis());
        params.put("authtoken", "");
        params.put("category_id", "");
        params.put("data_source", "xichou");
        params.put("type", "pc");
        params.put("version", "1.7.7");

        String result = HttpRequest.post(url).json(params).body();
        JsonNode json = JsonUtils.json(result, "data", "right_list");
        List<String> columnNames = Arrays.asList(
                "id",
                "gu_date",
                "gu_name",
                "gu_code",
                "gu_pe",
                "gu_pe_current_perent",
                "gu_pb",
                "gu_pb_current_perent",
                "gu_xilv",
                "gu_xilv_current_perent",
                "gu_up_down_fu",
                "gu_up_down_year"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
        return table;
    }

    /**
     * @param symbol
     * @param type   可选值：pe,pb,xilv
     * @return
     */
    public static Table indexValuation(String symbol, String type) {
        String url = "https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata";

        Map<String, Object> params = new HashMap<>();
        params.put("act_time", 1634821370997L);
        params.put("authtoken", "");
        params.put("data_source", "xichou");
        params.put("gu_code", symbol);
        params.put("pe_category", type/*"pe"*/);
        params.put("type", "pc");
        params.put("version", "1.7.7");
        params.put("year", -1);

        String result = HttpRequest.post(url).json(params).body();
        JsonNode series = JsonUtils.json(result, "data", "tubiao", "series");

        List<String> columnNames = Arrays.asList("timestamp", "value");

        Table table = null;
        for (int i = 0; i < 6; i++) {
            JsonNode json = series.get(i).get("data");
            List<String[]> dataRows = JsonUtils.extractArrayData(json);

            Table t = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());

            if (null == table) {
                table = t;
            } else {
                table = table.joinOn("timestamp").inner(true, t);
            }
            table.column("value").setName(series.get(i).get("name").asText());
        }

        // 时间戳转换成时间
        DateTimeColumn dateTimeColumn = table.longColumn("timestamp").asDateTimes(ZoneOffset.ofHours(+8));
        table.replaceColumn("timestamp", dateTimeColumn.date().setName("day"));

        return table;
    }

    public static void main(String[] args) {
        String symbol = "000028.SH";

        Table table = indexValuation(symbol, "pb");

//        Table table = indexValuation();
        System.out.println(table.print());
        System.out.println(table.structure().print());
        System.out.println(table.shape());
    }
}
