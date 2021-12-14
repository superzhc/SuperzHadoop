package com.github.superzhc.data.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * @author superz
 * @create 2021/12/15 10:10
 */
public class ExcelData implements FileData {
    private static final Logger log = LoggerFactory.getLogger(ExcelData.class);

    private String path;
    private Workbook workbook;

    public ExcelData(String path) {
        this.path = path;
        init();
    }

    private void init() {
        if (null == path || path.trim().length() == 0) {
            throw new RuntimeException("文件地址不能为空");
        }

        try {
            InputStream in = new FileInputStream(path);

            String ext = path.substring(path.lastIndexOf(".") + 1);

            if ("xls".equals(ext)) {
                workbook = new HSSFWorkbook(in);
            } else if ("xlsx".equals(ext)) {
                workbook = new XSSFWorkbook(in);
            } else {
                throw new RuntimeException("非法后缀 " + ext);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void preview(Integer number) {
        String printTemplate = "Sheet[%s],Total:%d\n%s";
        // sheet数
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();

            StringBuilder content = new StringBuilder();
            // 总行数
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            int n = Math.min(numberOfRows, number);
            for (int j = 0; j < n; j++) {
                Row row = sheet.getRow(j);
                int numberOfCells = row.getPhysicalNumberOfCells();
                for (int k = 0; k < numberOfCells; k++) {
                    Cell cell = row.getCell(k);
                    content.append("|").append(getCellFormatValue(cell));
                }
                content.append("|\n");
            }
            System.out.printf(printTemplate, sheetName, numberOfRows, content);
        }
    }

    public void count() {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            System.out.printf("Sheet[%s] count : %d\n", sheetName, numberOfRows);
        }
    }

    /**
     * 将单元格内容转换为字符串
     *
     * @param cell
     * @return
     */
    private Object getCellFormatValue(Cell cell) {
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
                    // 格式化科学计数法，取一位整数
                    DecimalFormat df = new DecimalFormat("0");
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
