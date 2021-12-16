package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.utils.ExcelUtils;
import com.github.superzhc.data.utils.PinYinUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

//            if ("xls".equalsIgnoreCase(ext)) {
//                workbook = new HSSFWorkbook(in);
//            } else if ("xlsx".equalsIgnoreCase(ext)) {
//                workbook = new XSSFWorkbook(in);
//            } else {
//                throw new RuntimeException("非法后缀 " + ext);
//            }
            // fix bug:有些excel文件是07版本的，但后缀依旧使用了xls，这会造成使用错误的Workbook，现改为poi通过流自行判断
            if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
                workbook = WorkbookFactory.create(in);
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
//                int numberOfCells = row.getPhysicalNumberOfCells();
//                for (int k = 0; k < numberOfCells; k++) {
//                    Cell cell = row.getCell(k);
//                    content.append("|").append(ExcelUtils.getCellFormatValue(cell));
//                }
//                content.append("|\n");
                content.append(ExcelUtils.format(row)).append("\n");
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

    protected void ddl() {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            if (numberOfRows < 2) {
                continue;
            }

            StringBuilder columnsStr = new StringBuilder();
            // 通过头行获取所有列
            Row headerRow = sheet.getRow(0);
            int headerNumberOfCells = headerRow.getPhysicalNumberOfCells();
            for (int j = 0; j < headerNumberOfCells; j++) {
                columnsStr.append(",").append(PinYinUtils.pinyin(headerRow.getCell(j).getStringCellValue())).append(" varchar(255)\n");
            }

            // region 推测类型不够准确，不怎么好用，后期优化这部分，目前全部设置成varchar
//            String[] columns = new String[headerNumberOfCells];
//            Integer[] columnsIndex = new Integer[headerNumberOfCells];
//            for (int j = 0; j < headerNumberOfCells; j++) {
//                columns[j] = PinYinUtils.pinyin((String) getCellFormatValue(headerRow.getCell(j)));
//                columnsIndex[j] = j;
//            }
//
//            // 通过数据行获取所有列的类型
//            Map<String, String> columnInfos = new HashMap<>();
//            Integer[] oldRemain = columnsIndex;
//            for (int k = 1; k < numberOfRows; k++) {
//                List<Integer> newRemain = new ArrayList<>();
//
//                Row dataRow = sheet.getRow(k);
//                for (int m = 0, len = oldRemain.length; m < len; m++) {
//                    Cell cell = dataRow.getCell(oldRemain[m]);
//                    if (NUMERIC == cell.getCellType()) {
//                        if (DateUtil.isCellDateFormatted(cell)) {
//                            columnInfos.put(columns[oldRemain[m]], "datetime");
//                        } else {
//                            columnInfos.put(columns[oldRemain[m]], "int");
//                        }
//                    } else if (BOOLEAN == cell.getCellType()) {
//                        columnInfos.put(columns[oldRemain[m]], "int");
//                    } else if (STRING == cell.getCellType()) {
//                        columnInfos.put(columns[oldRemain[m]], "varchar(255)");
//                    } else {
//                        newRemain.add(oldRemain[m]);
//                    }
//                }
//
//                if (newRemain.size() == 0) {
//                    oldRemain = null;
//                    break;
//                }
//
//                Integer[] remainArr = new Integer[newRemain.size()];
//                newRemain.toArray(remainArr);
//                oldRemain = remainArr;
//            }
//
//            if (null != oldRemain || oldRemain.length > 0) {
//                for (Integer index : oldRemain) {
//                    columnInfos.put(columns[index], "varchar(255)");
//                }
//            }
//
//            StringBuilder sb = new StringBuilder();
//            for (Map.Entry<String, String> entry : columnInfos.entrySet()) {
//                sb.append(",").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
//            }
            // endregion
            String DDLSql = String.format("create table if not exists %s\n(\nid int auto_increment primary key\n%s)", sheetName, columnsStr);
            System.out.println(DDLSql);
            System.out.println();
        }
    }

    public int numberOfSheets() {
        return workbook.getNumberOfSheets();
    }

    public boolean hasData(Integer sheetNumber) {
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        return numberOfRows > 0;
    }

    public String[] columns(Integer sheetNumber) {
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        String sheetName = sheet.getSheetName();
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        if (numberOfRows < 1) {
            throw new RuntimeException("Sheet[" + sheetName + "]无数据");
        }

        // 通过头行获取所有列
        Row headerRow = sheet.getRow(0);
        int headerNumberOfCells = headerRow.getPhysicalNumberOfCells();
        String[] arr = new String[headerNumberOfCells];
        for (int j = 0; j < headerNumberOfCells; j++) {
            String pinyin = PinYinUtils.pinyin(headerRow.getCell(j).getStringCellValue());
            arr[j] = (null == pinyin || pinyin.trim().length() == 0) ? "note" : pinyin;
        }
        return arr;
    }

    /**
     * 虚拟列，获取列的数数量，自动生成列数
     *
     * @param sheetNumber
     * @return
     */
    public String[] virtualColumns(Integer sheetNumber) {
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        String sheetName = sheet.getSheetName();
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        if (numberOfRows < 1) {
            throw new RuntimeException("Sheet[" + sheetName + "]无数据");
        }

        Row row = sheet.getRow(0);
        int numberOfCells = row.getPhysicalNumberOfCells();

        String[] columns = new String[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {
            columns[i] = "c" + (i + 1);
        }
        return columns;
    }

    public void write2db(String url, String username, String password, String table, Integer sheetNumber) {
        write2db(url, username, password, table, virtualColumns(sheetNumber), sheetNumber, 0);
    }

    public void write2db(String url, String username, String password, String table, String[] columns, Integer sheetNumber) {
        write2db(url, username, password, table, columns, sheetNumber, 0);
    }

    public void write2db(String url, String username, String password, String table, String[] columns, Integer sheetNumber, Integer headers) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {

            // 判断表是否存在
            if (jdbc.exist(table)) {
                throw new RuntimeException("表[" + table + "]已存在");
            }

            // 是否自带id列，默认是不带的
            boolean idFlag = false;
            // 创建表
            StringBuilder columnsStr = new StringBuilder();
            for (String column : columns) {
                if ("id".equalsIgnoreCase(column)) {
                    idFlag = true;
                }
                columnsStr.append(",").append(PinYinUtils.pinyin(column)).append(" varchar(255)");
            }
            // 自带id列不可用自增
            String idStr;
            if (!idFlag) {
                idStr = "id int auto_increment primary key";
            } else {
                idStr = "uid int auto_increment primary key";
            }
            String ddl = String.format("create table if not exists %s(%s%s) ENGINE=MyISAM", table, idStr, columnsStr);
            int result = jdbc.ddlExecute(ddl);
            if (result == -1) {
                throw new RuntimeException("创建表[" + table + "]失败");
            }

            transform2db(jdbc, table, columns, sheetNumber, headers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void append2db(String url, String username, String password, String table, String[] columns, Integer sheetNumber, Integer headers) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            transform2db(jdbc, table, columns, sheetNumber, headers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void transform2db(JdbcHelper jdbc, String table, String[] columns, Integer sheetNumber, Integer headers) {
        // 组织数据进行插入
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        if (headers < numberOfRows) {
            ErrorData error = new ErrorData(table);
            List<List<Object>> values = new ArrayList<>();
            for (int i = headers; i < numberOfRows; i++) {
                List<Object> value = new ArrayList<>();
                Row row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }

                int numberOfCells = row.getPhysicalNumberOfCells();

                if (numberOfCells != columns.length) {
                    error.add(ExcelUtils.json(row));
                    // log.error("错误数据:" + ExcelUtils.format(row));
                    continue;
                }

                for (int k = 0; k < numberOfCells; k++) {
                    Cell cell = row.getCell(k);
                    Object obj = ExcelUtils.getCellFormatValue(cell);
                    if (String.valueOf(obj).length() > 255) {
                        error.add(ExcelUtils.json(row));
                        break;
                    }
                    value.add(obj);
                }
                values.add(value);
            }
            log.debug("数据总条数（不包含错误数据）：" + values.size());
            log.debug("错误数据条数：" + error.total());
            jdbc.batchUpdate(table, columns, values, 1000);
            error.write2db(jdbc);
        }
    }

    public static void excel2db(String path, String url, String username, String password) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("地址[" + path + "]不存在");
        }

        excel2db(file, url, username, password);
    }

    private static void excel2db(File file, String url, String username, String password) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                excel2db(child, url, username, password);
            }
        } else {
            try {
                ExcelData data = new ExcelData(file.getPath());

                String fileName = file.getName();
                fileName = PinYinUtils.pinyin(fileName);
                String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
                String schema = "car_import_"
                        + fileNameWithoutExt.replace(".", "")
                        .replace("(", "").replace(")", "")
                        .replace("=", "")
                        .replace("-", "_").replace(" ", "_");

                int numberOfSheets = data.numberOfSheets();
                if (numberOfSheets > 0) {
                    for (int i = 0; i < numberOfSheets; i++) {
                        if (!data.hasData(i)) {
                            continue;
                        }

                        schema += "_" + (i + 1);
                        data.write2db(url, username, password, schema, i);
                    }
                }
            } catch (Exception e) {
                log.error("文件[" + file.getPath() + "]异常", e);
                return;
            }
        }
    }

    public static void main(String[] args) {
        String path = "";
        //path="D:\\downloads\\Chrome\\WorldCupMatches.xls";
        path = "D:\\downloads\\baidu\\car\\";
        path += "xxx.XLS";

        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";


//        excel2db(path,url , username, password);


        ExcelData data = new ExcelData(path);
        data.preview();
//
        String schema = "car_zhejiang_jiashizheng";
        Integer sheetNumber = 0;

        // 有标题头
        Integer headers = 2;
        String[] columns = data.columns(sheetNumber);

//        String[] columns = data.virtualColumns(sheetNumber);

        // 无标题头
//        Integer headers = 0;
//        String[] columns = new String[]{"xingming", "zhuangtai", "dianhuahaoma", "dengjizhusuo", "zhengjianhaoma", "nianyueri", "xingbie", "weishu", "canshu", "jieguo"};

        System.out.println(Arrays.asList(columns));

//        data.write2db(url, username, password, schema, columns, sheetNumber, headers);
//        data.append2db(url, username, password, schema, columns, sheetNumber, headers);
    }


}
