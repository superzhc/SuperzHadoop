package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.data.utils.ExcelUtils;
import com.github.superzhc.fund.common.HttpConstant;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.io.InputStream;
import java.util.*;

/**
 * 官网：https://www.csindex.com.cn
 * 参考：https://github.com/akfamily/akshare/blob/master/akshare/index/zh_stock_index_csindex.py
 *
 * @author superz
 * @create 2022/4/6 17:59
 **/
public class CSIndex {

    private static final Logger log = LoggerFactory.getLogger(CSIndex.class);

    public static Table indics() {
        String url = "https://www.csindex.com.cn/csindex-home/index-list/query-index-item";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", HttpConstant.UA);
        headers.put("Content-Type", HttpConstant.JSON_CONTENT_TYPE);

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
                "indexCode",
                "indexName",
                "indexNameEn",
                "consNumber",
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

    /**
     * 获取指数的历史数据
     * <p>
     * 不推荐使用该接口，获取的数据不够全
     *
     * @param symbol
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
    public static Table indexHistory(String symbol) {
        String url = "https://www.csindex.com.cn/csindex-home/perf/index-perf";

        Map<String, String> params = new HashMap<>();
        params.put("indexCode", symbol/*"H30374"*/);
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

    public static Table indexValue(String symbol) {
        String url = String.format("https://csi-web-dev.oss-cn-shanghai-finance-1-pub.aliyuncs.com/static/html/csindex/public/uploads/file/autofile/indicator/%sindicator.xls", symbol);

        try {
            InputStream in = HttpRequest.get(url).stream();
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            List<String> columnNames = new ArrayList<>();
            Row header = sheet.getRow(0);
            int headerNumberOfCells = header.getPhysicalNumberOfCells();
            for (int h = 0; h < headerNumberOfCells; h++) {
                columnNames.add(String.valueOf(ExcelUtils.getCellFormatValue(header.getCell(h))));
            }

            List<String[]> dataRows = new ArrayList<>();
            for (int i = 1; i < numberOfRows; i++) {
                Row row = sheet.getRow(i);
                int numberOfCells = row.getPhysicalNumberOfCells();
                String[] dataRow = new String[numberOfCells];
                for (int j = 0; j < numberOfCells; j++) {
                    Cell cell = row.getCell(j);
                    Object value = ExcelUtils.getCellFormatValue(cell);
                    dataRow[j] = null == value ? null : String.valueOf(value);
                }
                dataRows.add(dataRow);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
