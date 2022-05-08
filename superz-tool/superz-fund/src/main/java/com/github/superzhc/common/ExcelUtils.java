package com.github.superzhc.common;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.apache.poi.ss.usermodel.*;
import tech.tablesaw.api.Table;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2022/5/9 0:43
 */
public class ExcelUtils {
    public static Table read(InputStream in) {
        return read(in, 0);
    }

    public static Table read(InputStream in, int sheetNo) {
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(sheetNo);
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            List<String> columnNames = new ArrayList<>();
            Row header = sheet.getRow(0);
            int headerNumberOfCells = header.getPhysicalNumberOfCells();
            for (int h = 0; h < headerNumberOfCells; h++) {
                columnNames.add(String.valueOf(com.github.superzhc.data.utils.ExcelUtils.getCellFormatValue(header.getCell(h))));
            }

            List<String[]> dataRows = new ArrayList<>();
            for (int i = 1; i < numberOfRows; i++) {
                Row row = sheet.getRow(i);
                int numberOfCells = row.getPhysicalNumberOfCells();
                String[] dataRow = new String[numberOfCells];
                for (int j = 0; j < numberOfCells; j++) {
                    Cell cell = row.getCell(j);
                    Object value = com.github.superzhc.data.utils.ExcelUtils.getCellFormatValue(cell);
                    dataRow[j] = null == value ? null : String.valueOf(value);
                }
                dataRows.add(dataRow);
            }

            Table table = TableUtils.build(columnNames, dataRows);
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
