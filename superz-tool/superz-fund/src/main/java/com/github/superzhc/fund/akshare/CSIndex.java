package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.data.utils.ExcelUtils;
import com.github.superzhc.fund.tablesaw.utils.ColumnUtils;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import org.apache.poi.ss.usermodel.*;
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

    public static Table indics() {
        String url = "https://www.csindex.com.cn/csindex-home/index-list/query-index-item";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
        headers.put("Content-Type", "application/json;charset=UTF-8");

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

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("key", ColumnType.STRING);
        columnTypeMap.put("indexCode", ColumnType.STRING);
        columnTypeMap.put("hotSpot", ColumnType.STRING);
        columnTypeMap.put("hotSpotEn", ColumnType.STRING);

        Table table = null;

        Integer size = null;
        Integer currentPage = 1;
        Integer pageSize = 10;
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

        return table;
    }

    public static Table historyIndex(String symbol) {
        String url = "https://www.csindex.com.cn/csindex-home/perf/index-perf";

        Map<String, String> params = new HashMap<>();
        params.put("indexCode", symbol/*"H30374"*/);
        // 时间必须要有
        params.put("startDate", "19900101");
        params.put("endDate", "20991231");

        String result = HttpRequest.get(url, params).body();

        List<String> columnNames = ColumnUtils.transform(
                "日期",
                "指数代码",
                "指数中文全称",
                "指数中文简称",
                "指数英文全称",
                "指数英文简称",
                "开盘",
                "最高",
                "最低",
                "收盘",
                "涨跌",
                "涨跌幅",
                "成交量",
                "成交金额",
                "样本数量"
        );
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
        Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));
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
        Table table = indics();//historyIndex("000300");
        System.out.println(table.print());
        System.out.println(table.structure().print());
    }
}
