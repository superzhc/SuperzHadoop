package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.*;
import tech.tablesaw.io.TableBuildingUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/13 22:45
 */
public class GuBaIndex {

    public static Table indices() {
        int pageSize = 50;
        int page = 1;
        Integer pageCount = null;

        String url = "https://www.gubafund.com/api/index/index_list.json";

        Map<String, Object> params = new HashMap<>();
        params.put("search", "{}");
        params.put("type", "1");
        params.put("order", "");

        List<String> columnNames = Arrays.asList(
                "operat_companies",// 运营公司
                "cover_market", // 覆盖市场
                "index_code",// 指数代码
                "index_name",// 指数名称
                "index_total_name",// 指数全名称
                "indice_type",// 指数类型
                "category",
                "line_date",// 更新日期
                "total_value",// 市值（亿）
                "end",// 收盘点
                "today",// 当天涨跌幅（%）
                "pe",// PE-TTM
                "pe_per",// PE 百分位
                "pb",// PB
                "pb_per",// PB百分位
                "ps",// PS
                "ps_per",// PS 百分位
                "dividend_ratio",// 股息率
                "dividend_per",//股息百分位
                "roe",// ROE
                "roe_date",
                "cdd",// 连跌天数
                "cud",// 连涨天数
                "week",// 近一周
                "month",// 近一月
                "tmonth",// 本月
                "three_month",// 近三月
                "six_month",// 近六月
                "year",// 近一年
                "tyear",// 当年
                "two_year",// 近两年
                "three_year",// 近三年
                "ett",// 成立至今
                "year_ratio_two",// 年化收益率-近两年
                "year_ratio_three",// 年化收益率-近三年
                "year_ratio_start",// 年化收益率-发布至今
                "fund_count",// 跟踪基金
                "base_date",//基日
                "base_num",//基点
                "publish_date",//发布日
                "elements",//成分股
                "bzfa_url"/*:"http://www.cnindex.com.cn/docs/gz_399001.pdf"*/,// 编制方案
                "elements_url"/*:"http://www.cnindex.com.cn/docs/yb_399001.xls"*/,// 成分股
                // "weight_url":"",
                // "weighting_scheme":"自由流通股本",
                // "is_spider":1,
                // "spider_code":null,
                // "spider_url":"dongfangcaifu,guozheng",
                // "description":"深证成指由深圳证券市场中市值大、流动性好的500只A股组成，定位于深交所标尺指数，反映深交所多层次市场的整体表现。",
                "description"// 描述
        );

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("index_code", ColumnType.STRING);
        columnTypeMap.put("base_num", ColumnType.DOUBLE);

        Table table = null;
        while (null == pageCount || page <= pageCount) {
            params.put("page", page++);
            params.put("size", pageSize);

            String result = HttpRequest.get(url, params).body();
            JsonNode tableData = JsonUtils.json(result, "data", "tables_data");

            if (null == pageCount) {
                int count = tableData.get("count").asInt();
                pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
            }

            JsonNode json = tableData.get("results");
            List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

            Table t2 = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(columnTypeMap).build());
            if (null == table) {
                table = t2;
            } else {
                table = table.append(t2);
            }
        }

        return table;
    }

    public static Table index(String symbol) {
        String indexCode = transform(symbol);
        String url = String.format("https://www.gubafund.com/api/index/index_list/%s.json", indexCode);

        Map<String, Object> params = new HashMap<>();
        params.put("code", indexCode);

        String result = HttpRequest.get(url, params).body();
        Map<String, ?> map = JsonUtils.map(result, "data");
        Table table = TableUtils.map2Table(map);
        return table;
    }

    /**
     * 时间不是连续的
     *
     * @param symbol
     *
     * @return
     */
    public static Table valuation(String symbol) {
        String indexCode = transform(symbol);

        String url = String.format("https://www.gubafund.com/api/index/index_list/%s.json", indexCode);

        Map<String, Object> params = new HashMap<>();
        params.put("custom_type", "get_k_lines");

        Map<String, Object> body = new HashMap<>();
        body.put("index_code", indexCode);
        body.put("time_quantum", "all");
        body.put("start_date", "1990-01-01");
        body.put("end_date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String result = HttpRequest.post(url, params).json(body).body();
        JsonNode json = JsonUtils.json(result, "data");

        DateColumn date = DateTimeColumn.create("date", JsonUtils.long2DateTimeArray(json.get("line_date"))).date();
        DoubleColumn closed = DoubleColumn.create("closed", JsonUtils.doubleArray(json.get("end")));
        DoubleColumn totalValue = DoubleColumn.create("total_value", JsonUtils.doubleArray(json.get("total_value")));
        DoubleColumn pb = DoubleColumn.create("pb", JsonUtils.doubleArray(json.get("pb")));
        DoubleColumn pe = DoubleColumn.create("pe", JsonUtils.doubleArray(json.get("pe")));
        DoubleColumn ps = DoubleColumn.create("ps", JsonUtils.doubleArray(json.get("ps")));
        DoubleColumn roe = DoubleColumn.create("roe", JsonUtils.doubleArray(json.get("dividend_ratio")));

        Table table = Table.create(String.format("%s[%s]", json.get("index_name").asText(), json.get("index_code").asText()),
                date,
                closed,
                totalValue,
                pb,
                pe,
                ps,
                roe
        );

        return table;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) {
        Table table = valuation("399001.SZ");

        System.out.println(table.printAll());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
        // System.out.println(table.structure().stringColumn(1).map(d -> String.format("\"%s\"", d)).asList());
    }
}
