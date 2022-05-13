package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.ExcelUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.HttpConstant;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.io.InputStream;
import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * 中证指数
 * 官网：https://www.csindex.com.cn
 * 参考：https://github.com/akfamily/akshare/blob/master/akshare/index/zh_stock_index_csindex.py
 * <p>
 * 2022年5月13日 官方接口目前需要设置 Cookies 才能获取到数据，根据响应代码分析，引入的是阿里去验证码脚本，即 nc.js，手动添加 Cookie；注：过期时间很快~~~
 *
 * @author superz
 * @create 2022/4/6 17:59
 **/
public class CSIndex {

    private static final Logger log = LoggerFactory.getLogger(CSIndex.class);

    public static String cookies = null;

    public static void setCookies(String cookies) {
        CSIndex.cookies = cookies;
    }

    public static Table indices() {
        String url = "https://www.csindex.com.cn/csindex-home/index-list/query-index-item";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);
        headers.put("Content-Type", HttpConstant.JSON_CONTENT_TYPE);
        if (null != cookies) {
            headers.put("Cookie", cookies);
        }

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> indexFilter = new HashMap<>();
        indexFilter.put("ifCustomized", null);
        indexFilter.put("ifTracked", null);
        indexFilter.put("ifWeightCapped", null);
        indexFilter.put("indexCompliance", null);
        indexFilter.put("hotSpot", null);
        indexFilter.put("indexClassify", null);
        indexFilter.put("currency", null);
        indexFilter.put("region", null);
        indexFilter.put("indexSeries", null);
        indexFilter.put("undefined", null);

        // Map<String, Object> pager = new HashMap<>();
        // pager.put("pageNum", 1);
        // pager.put("pageSize", 10);

        Map<String, String> sorter = new HashMap<>();
        sorter.put("sortField", "null");
        sorter.put("sortOrder", null);

        params.put("indexFilter", indexFilter);
        params.put("sorter", sorter);

        List<String> columnNames = Arrays.asList(
                "indexCompliance",
                "indexComplianceEn",
                "ifTracked",
                "ifTrackedEn",
                "indexSeries",
                "indexSeriesEn",
                "key",
                "indexCode",// 指数代码
                "indexName",// 指数名称
                "indexNameEn",
                "consNumber",// 样本数量
                "latestClose",
                "monthlyReturn",
                "indexType",
                "assetsClassify",
                "assetsClassifyEn",
                "hotSpot",
                "hotSpotEn",
                "region",
                "regionEn",
                "currency",
                "currencyEn",
                "ifCustomized",
                "ifCustomizedEn",
                "indexClassify",
                "indexClassifyEn",
                "ifWeightCapped",
                "ifWeightCappedEn",
                "publishDate"
        );

        /* 字段类型映射，消除自动判断映射的错误 */
        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("key", ColumnType.STRING);
        columnTypeMap.put("indexCode", ColumnType.STRING);
        columnTypeMap.put("hotSpot", ColumnType.STRING);
        columnTypeMap.put("hotSpotEn", ColumnType.STRING);

        Table table = null;

        Integer size = null;
        Integer currentPage = 1;
        Integer pageSize = 2300;
        while (null == size || currentPage <= size) {
            Map<String, Object> pager = new HashMap<>();
            pager.put("pageNum", currentPage);
            pager.put("pageSize", pageSize);
            params.put("pager", pager);

            String result = HttpRequest.post(url).headers(headers).json(params).body();
            JsonNode json = JsonUtils.json(result);
            size = json.get("size").asInt();

            List<String[]> dataRows = JsonUtils.extractObjectData(json.get("data"), columnNames);
            Table t = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));
            if (null == table) {
                table = t;
            } else {
                table.append(t);
            }

            currentPage++;
        }

        table.removeColumns("key");
        return table;
    }

    /*public static Table indices(String str) {
        Table table = indices();

        if (null != str && str.trim().length() > 0) {
            Selection condition = table.stringColumn("indexName").containsString(str)
                    .or(table.stringColumn("indexCode").containsString(str));

            table = table.where(condition);
        }

        return table;
    }*/

    public static Table indices(String search) {
        String url = "https://www.csindex.com.cn/csindex-home/index-list/search-result-about-index";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);
        headers.put("Content-Type", HttpConstant.JSON_CONTENT_TYPE);
        if (null != cookies) {
            headers.put("Cookie", cookies);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("searchInput", search);
        params.put("pageNum", 1);
        params.put("pageSize", 100);

        String result = HttpRequest.get(url, params).headers(headers).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = Arrays.asList(
                // "indexCompliance",
                // "indexComplianceEn",
                // "ifTracked",
                // "ifTrackedEn",
                // "indexSeries",
                // "indexSeriesEn",
                // "key",
                "indexCode",// 指数代码
                "indexName",// 指数名称
                "indexNameEn",
                "consNumber",// 样本数量
                "latestClose",
                "monthlyReturn",
                "indexType",
                "assetsClassify",
                "assetsClassifyEn",
                "hotSpot",
                "hotSpotEn",
                "region",
                "regionEn",
                "currency",
                "currencyEn",
                "ifCustomized",
                "ifCustomizedEn",
                "indexClassify",
                "indexClassifyEn",
                "ifWeightCapped",
                "ifWeightCappedEn",
                "publishDate"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table index(String indexCode) {
        String symbol = transform(indexCode);
        // 指数基本信息
        String basicUrl = String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-basic-info/%s", symbol);

        String basicResult = HttpRequest.get(basicUrl).userAgent(UA).cookies(cookies).body();
        Map<String, ?> basicMap = JsonUtils.map(basicResult, "data");

        // 指数特征
        String featureUrl = String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-feature/%s", symbol);
        String featureResult = HttpRequest.get(featureUrl).userAgent(UA).cookies(cookies).body();
        Map<String, ?> featureMap = JsonUtils.map(featureResult, "data");

        // other
        String otherUrl = String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-details-data?fileLang=2&indexCode=%s", symbol);
        String otherResult = HttpRequest.get(otherUrl).userAgent(UA).cookies(cookies).body();
        JsonNode other = JsonUtils.json(otherResult, "data");

        Map<String, Object> map = new LinkedHashMap<>();
        map.putAll(basicMap);
        map.putAll(featureMap);

        map.put("preparation", other.get("编制方案").get(0).get("filePath").asText());

        Table table = TableUtils.map2Table(map);

        return table;
    }

    /*public static Table feature(String indexCode){
        String symbol=transform(indexCode);

        String url=String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-feature/%s",symbol);
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data");

        Map<String, ?> map = JsonUtils.map(json);

        Table table = TableUtils.map2Table(map);

        return table;

    }*/

    public static Table trackIndex(String indexCode) {
        String symbol = transform(indexCode);
        String url = String.format("https://www.csindex.com.cn/csindex-home/index-list/queryByIndexCode/%s?indexCode=%s", symbol, symbol);

        String result = HttpRequest.get(url).userAgent(UA).cookies(cookies).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table mainStocks(String indexCode) {
        String url = String.format("https://www.csindex.com.cn/csindex-home/index/weight/top10/%s", transform(indexCode));

        String result = HttpRequest.get(url).userAgent(UA).cookies(cookies).body();
        JsonNode json = JsonUtils.json(result, "data", "weightList");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    /**
     * 行业分布
     *
     * @param indexCode
     * @return
     */
    public static Table industry(String indexCode) {
        String url = String.format("https://www.csindex.com.cn/csindex-home/index/weight/industry-weight/%s", transform(indexCode));

        String result = HttpRequest.get(url).userAgent(UA).cookies(cookies).body();
        JsonNode json = JsonUtils.json(result, "data", "industryWeightList");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table markets(String indexCode) {
        String url = String.format("https://www.csindex.com.cn/csindex-home/index/weight/market-weight/%s", transform(indexCode));

        String result = HttpRequest.get(url).userAgent(UA).cookies(cookies).body();
        JsonNode json = JsonUtils.json(result, "data", "marketWeightList");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    /**
     * 获取指数的历史数据
     * <p>
     * 不推荐使用该接口，获取的数据不够全
     *
     * @param indexCode
     * @return Table
     * Index  |  Column Name  |  Column Type  |
     * -----------------------------------------
     * 0  |           日期  |   LOCAL_DATE  |
     * 1  |         指数代码  |       STRING  |
     * 2  |       指数中文全称  |       STRING  |
     * 3  |       指数中文简称  |       STRING  |
     * 4  |       指数英文全称  |       STRING  |
     * 5  |       指数英文简称  |       STRING  |
     * 6  |           开盘  |       DOUBLE  |
     * 7  |           最高  |       DOUBLE  |
     * 8  |           最低  |       DOUBLE  |
     * 9  |           收盘  |       DOUBLE  |
     * 10  |           涨跌  |       DOUBLE  |
     * 11  |          涨跌幅  |       DOUBLE  |
     * 12  |          成交量  |       DOUBLE  |
     * 13  |         成交金额  |       DOUBLE  |
     * 14  |         样本数量  |      INTEGER  |
     */
    @Deprecated
    public static Table history(String indexCode) {
        String url = "https://www.csindex.com.cn/csindex-home/perf/index-perf";

        Map<String, String> params = new HashMap<>();
        params.put("indexCode", transform(indexCode)/*"H30374"*/);
        // 时间必须要有
        params.put("startDate", "19900101");
        params.put("endDate", "20991231");

        String result = HttpRequest.get(url, params).body();

//        List<String> columnNames = ColumnUtils.transform(
//                "日期",
//                "指数代码",
//                "指数中文全称",
//                "指数中文简称",
//                "指数英文全称",
//                "指数英文简称",
//                "开盘",
//                "最高",
//                "最低",
//                "收盘",
//                "涨跌",
//                "涨跌幅",
//                "成交量",
//                "成交金额",
//                "样本数量"
//        );
        List<String> originColumnNames = Arrays.asList(
                "tradeDate",
                "indexCode",
                "indexNameCnAll",
                "indexNameCn",
                "indexNameEnAll",
                "indexNameEn",
                "open",
                "high",
                "low",
                "close",
                "change",
                "changePct",
                "tradingVol",
                "tradingValue",
                "consNumber"
        );
        List<String[]> dataRows = JsonUtils.extractObjectData(JsonUtils.json(result, "/data"), originColumnNames);

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("指数代码", ColumnType.STRING);
        columnTypeMap.put("indexCode", ColumnType.STRING);
        Table table = TableBuildingUtils.build(originColumnNames/*columnNames*/, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));
        return table;
    }

    public static Table stocks(String indexCode) {
        String symbol = transform(indexCode);
        String url = String.format("https://csi-web-dev.oss-cn-shanghai-finance-1-pub.aliyuncs.com/static/html/csindex/public/uploads/file/autofile/cons/%scons.xls", symbol);
        InputStream in = HttpRequest.get(url).userAgent(UA).cookies(cookies).stream();
        return ExcelUtils.read(in);
    }

    public static Table stocksWeight(String indexCode) {
        String symbol = transform(indexCode);
        String url = String.format("https://csi-web-dev.oss-cn-shanghai-finance-1-pub.aliyuncs.com/static/html/csindex/public/uploads/file/autofile/closeweight/%scloseweight.xls", symbol);
        InputStream in = HttpRequest.get(url).userAgent(UA).cookies(cookies).stream();
        return ExcelUtils.read(in);
    }

    public static Table valuation(String indexCode) {
        String symbol = transform(indexCode);
        String url = String.format("https://csi-web-dev.oss-cn-shanghai-finance-1-pub.aliyuncs.com/static/html/csindex/public/uploads/file/autofile/indicator/%sindicator.xls", symbol);

        InputStream in = HttpRequest.get(url).userAgent(UA).cookies(cookies).stream();
        return ExcelUtils.read(in);
    }

    // 衍生品
    public static Table derivative(String indexCode) {
        String symbol = transform(indexCode);
        String url = "https://www.csindex.com.cn/csindex-home/perf/get-derivative-index";

        Map<String, Object> params = new HashMap<>();
        params.put("indexCode", symbol);

        String result = HttpRequest.get(url, params).userAgent(UA).cookies(cookies).body();
        JsonNode json = JsonUtils.json(result, "data");

        if (null == json || json.size() == 0) {
            return Table.create();
        }

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    private static String transform(String indexCode) {
        String[] ss = indexCode.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) {
//        Table table = indexHistory("399986");
        //table.stringColumn("indexClassify").setMissingTo("空");
//        System.out.println(table.print());
        //System.out.println(table.structure().print());

//        Table t2 = table.summarize("indexCode", count).by("indexClassify");
//        System.out.println(t2.printAll());

        String url =
                "http://www.csindex.com.cn/zh-CN/indices/index-detail/000001.SH"
                //"http://www.cnindex.com.cn/zh_indices/sese/index.html?act_menu=1&index_type=-1"
                ;
        String result = HttpRequest.get(url).body();
        System.out.println(result);
    }
}
