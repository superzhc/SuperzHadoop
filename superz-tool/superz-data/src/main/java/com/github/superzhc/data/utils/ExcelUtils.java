package com.github.superzhc.data.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.text.DecimalFormat;

/**
 * @author superz
 * @create 2021/12/15 11:46
 */
public class ExcelUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

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
        try {
            return mapper.writeValueAsString(objs);
        } catch (JsonProcessingException e) {
            return null;
        }
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
