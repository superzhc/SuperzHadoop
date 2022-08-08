package com.github.superzhc.financial.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.time.ZoneOffset;
import java.util.*;

import static com.github.superzhc.financial.utils.IndexConstant.*;

/**
 * 官网地址：https://www.funddb.cn/
 *
 * @author superz
 * @create 2022/4/7 11:40
 **/
public class JiuCaiShuoIndex {

    private static final Logger log = LoggerFactory.getLogger(JiuCaiShuoIndex.class);

    /**
     * 指数估值列表
     *
     * @return Structure of
     * Index  |       Column Name        |  Column Type  |
     * ----------------------------------------------------
     * 0  |                      id  |      INTEGER  |
     * 1  |                 gu_date  |   LOCAL_DATE  |
     * 2  |                 gu_name  |       STRING  |
     * 3  |                 gu_code  |       STRING  |
     * 4  |                   gu_pe  |       DOUBLE  |
     * 5  |    gu_pe_current_perent  |       DOUBLE  |
     * 6  |                   gu_pb  |       DOUBLE  |
     * 7  |    gu_pb_current_perent  |       DOUBLE  |
     * 8  |                 gu_xilv  |       DOUBLE  |
     * 9  |  gu_xilv_current_perent  |       DOUBLE  |
     * 10  |           gu_up_down_fu  |       DOUBLE  |
     * 11  |         gu_up_down_year  |       DOUBLE  |
     */
    public static Table indicesValuation() {
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
     * 追踪指数
     *
     * @param symbol
     * @return
     */
    @Deprecated
    public static Table indexTrack(String symbol) {
        String indexBasic = indexBasic(symbol);
        JsonNode funds = JsonUtils.json(indexBasic, "data", "fund_info");

        List<String> columnNames = Arrays.asList(
                "gu_code",
                "gu_name",
                "gu_fund_year_income"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(funds, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table indexInfo(String symbol) {
        String indexBasic = indexBasic(symbol);
        JsonNode json = JsonUtils.json(indexBasic, "data");

        Map<String, String> map = new LinkedHashMap<>();

        String name = json.get("gu_name").asText();
        map.put("名称", name);

        String startDate = json.get("start_time").asText();
        map.put("起始时间", startDate);

        String description = json.get("synopsis").asText();
        map.put("描述", description);

        String updateTime = json.get("update_time").asText();
        for (JsonNode attr : json.get("top_data")) {
            String attrNameEn = attr.get("attribute").asText();
            String attrName = attr.get("name").asText();

            String newValue = attr.get("new_value").get("value").asText();
            map.put(String.format("%s %s[%s]", updateTime, attrName, attrNameEn.toUpperCase())
                    , newValue);

            // 存疑，释义感觉有点问题
//            String newPercentValue = attr.get("new_percent_value").get("value").asText();
//            keyColumn.append(String.format("%s %s[%s]涨跌幅", updateTime, attrName, attrNameEn.toUpperCase()));
//            valueColumn.append(newPercentValue);
//
//            String newAvg = attr.get("new_avg").get("value").asText();
//            keyColumn.append(String.format("%s %s[%s]平均值", updateTime, attrName, attrNameEn.toUpperCase()));
//            valueColumn.append(newAvg);
        }

        Table table = TableUtils.map2Table(map);
        return table;
    }

    /**
     * 主要组成
     *
     * @param symbol
     * @return
     */
    public static Table indexComponent(String symbol) {
        String indexBasic = indexBasic(symbol);
        JsonNode json = JsonUtils.json(indexBasic, "data", "cl_many_info");

        List<String> columnNames = Arrays.asList(
                "name",
                "code",
                "market_value",
                "weight"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    private static String indexBasic(String symbol) {
        String url = "https://api.jiucaishuo.com/v2/guzhi/newtubiaodata";

        Map<String, Object> params = new HashMap<>();
        params.put("gu_code", symbol);
        params.put("year", -1);
        params.put("ver", "new");

        String result = HttpRequest.post(url).json(params).body();
        return result;
    }

    public static Table pe(String symbol) {
        return indexValuation(symbol, INDEX_VALUATION_PE);
    }

    public static Table pb(String symbol) {
        return indexValuation(symbol, INDEX_VALUATION_PB);
    }

    /**
     * @param symbol
     * @param type   可选值：pe,pb,xilv
     * @return
     */
    public static Table indexValuation(String symbol, String type) {
        String url = "https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata";

        Map<String, Object> params = new HashMap<>();
        params.put("act_time", System.currentTimeMillis());
        params.put("authtoken", "");
        params.put("data_source", "xichou");
        params.put("gu_code", symbol);
        params.put("pe_category", type/*"pe"*/);
        params.put("type", "pc");
        params.put("version", "1.7.7");
        params.put("year", -1);

        String result = HttpRequest.post(url).json(params).body();
        JsonNode series = JsonUtils.json(result, "data", "tubiao", "series");

        List<String> columnNames = Arrays.asList("timestamp", type/*"value"*/);

        List<String[]> dataRows = JsonUtils.extractArrayData(series.get(1).get("data"));

        Table table = TableUtils.build(columnNames, dataRows);

        // 时间戳转换成时间
        DateTimeColumn dateTimeColumn = table.longColumn("timestamp").asDateTimes(ZoneOffset.ofHours(+8));
        table.replaceColumn("timestamp", dateTimeColumn.date().setName("date"));

        // 确保时间排序
        table = table.sortDescendingOn("date");

        return table;
    }

    public static void main(String[] args) {
        String symbol = "000300.SH";

//        Table table = indexValuation(symbol, "pb");

//        Table table = indexValuation();

        Table table = indexInfo(symbol);

        //Table table = indexComponent(symbol);

        System.out.println(table.printAll());
//        System.out.println(table.structure().print());
        System.out.println(table.shape());


    }
}
