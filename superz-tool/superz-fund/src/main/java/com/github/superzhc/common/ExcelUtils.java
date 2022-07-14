package com.github.superzhc.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.apache.poi.ss.usermodel.*;
import tech.tablesaw.api.Table;

import java.io.InputStream;
import java.text.DecimalFormat;
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
                columnNames.add(String.valueOf(getCellFormatValue(header.getCell(h))));
            }

            List<String[]> dataRows = new ArrayList<>();
            for (int i = 1; i < numberOfRows; i++) {
                Row row = sheet.getRow(i);
                int numberOfCells = row.getPhysicalNumberOfCells();
                String[] dataRow = new String[numberOfCells];
                for (int j = 0; j < numberOfCells; j++) {
                    Cell cell = row.getCell(j);
                    Object value = getCellFormatValue(cell);
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

    public static String format(Row row) {
        return format(row, "|");
    }

    public static String format(Row row, String separator) {
        if (null == row) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        int numberOfCells = row.getPhysicalNumberOfCells();
        for (int k = 0; k < numberOfCells; k++) {
            Cell cell = row.getCell(k);
            content.append(separator).append(getCellFormatValue(cell));
        }
        content.append(separator);
        return content.toString();
    }

    public static String json(Row row) {
        Object[] objs = values(row);
        return JsonUtils.asString(objs);
    }

    public static Object[] values(Row row) {
        if (null == row) {
            return null;
        }

        int numberOfCells = row.getPhysicalNumberOfCells();
        Object[] objs = new Object[numberOfCells];
        for (int k = 0; k < numberOfCells; k++) {
            Cell cell = row.getCell(k);
            objs[k] = getCellFormatValue(cell);
        }
        return objs;
    }

    /**
     * 将单元格内容转换为字符串
     *
     * @param cell
     * @return
     */
    public static Object getCellFormatValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        Object value = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    Double doubleValue = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("0.00");
                    value = df.format(doubleValue);
                }
                break;
            case STRING:    //字符串
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                value = cell.getBooleanCellValue();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                value = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return value;
    }
}
