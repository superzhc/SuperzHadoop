package com.github.superzhc.fund.akshare;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.data.utils.ExcelUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import org.apache.poi.ss.usermodel.*;
import tech.tablesaw.io.TableBuildingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 以后合并到 CSIndex 文件中
 *
 * @author superz
 * @create 2022/4/7 9:23
 **/
public class CSIndex2 {

    public static tech.tablesaw.api.Table indexValue(String symbol) {
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

            tech.tablesaw.api.Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String symbol = "000300";
        tech.tablesaw.api.Table table = indexValue(symbol);
        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());

    }
}
