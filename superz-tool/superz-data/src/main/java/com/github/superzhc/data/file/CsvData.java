package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.FileData;
import com.github.superzhc.data.utils.PinYinUtils;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 直接使用TxtData
 * @author superz
 * @create 2021/12/16 16:18
 */
@Deprecated
public class CsvData /*implements FileData*/ {
//    private static final Logger log = LoggerFactory.getLogger(CsvData.class);
//
//    private static final String DEFAULT_CHARSET = "UTF-8";
//    private static final char DEFAULT_SEPARATOR = ICSVParser.DEFAULT_SEPARATOR;
//    private static final Integer DEFAULT_HEADERS = CSVReader.DEFAULT_SKIP_LINES;
//
//    private String path;
//    private String charset;
//    private Integer headers;
//    private char separator;
//
//    public CsvData(String path, String charset) {
//        this(path, charset, DEFAULT_SEPARATOR, DEFAULT_HEADERS);
//    }
//
//    public CsvData(String path, String charset, char separator, Integer headers) {
//        this.path = path;
//        this.charset = charset;
//        this.separator = separator;
//        this.headers = headers;
//    }
//
//    private void read(Function<List<String[]>, Void> headersFunction, Integer linesNum, Function<List<String[]>, Boolean> linesFunction) {
//        try (InputStream inputStream = new FileInputStream(new File(path))) {
//            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(charset))) {
//                CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
//                try (CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build()) {
//                    log.debug("文件开始读取...");
//                    long start = System.currentTimeMillis();
//                    long total = 0L;
//
//                    String[] values;
//
//                    Long currentLines = 0L;
//                    /* 处理文件头 */
//                    if (headers > 0) {
//                        List<String[]> headerLines = new ArrayList<>(headers);
//                        Integer headerCursor = headers;
//                        while (headerCursor > 0 && (values = reader.readNext()) != null) {
//                            currentLines++;
//                            headerLines.add(values);
//                            headerCursor--;
//                        }
//
//                        if (null != headersFunction) {
//                            headersFunction.apply(headerLines);
//                        } else {
//                            log.debug("untreated header data:" + headerLines);
//                        }
//                    }
//
//                    /* 处理文件内容 */
//                    List<String[]> lines = new ArrayList<>(linesNum);
//                    int currentLinesNum = 0;
//                    while ((values = reader.readNext()) != null) {
//                        currentLines++;
//                        lines.add(values);
//                        currentLinesNum++;
//                        total++;
//
//                        if (currentLinesNum >= linesNum) {
//                            log.debug("文件处理中，行号：[" + (currentLines - currentLinesNum + 1) + "~" + currentLines + "]");
//                            Boolean flag = linesFunction.apply(lines);
//                            currentLinesNum = 0;
//                            lines.clear();
//
//                            if (!flag) {
//                                total = 0L;
//                                break;
//                            }
//                        }
//                    }
//                    if (currentLinesNum > 0) {
//                        log.debug("文件处理中，行号：" + (currentLines - currentLinesNum + 1) + "~" + currentLines);
//                        linesFunction.apply(lines);
//                    }
//
//                    long end = System.currentTimeMillis();
//                    log.debug((total > 0 ? "文件处理完成，文件总行数：" + total + "，" : "") + "文件处理总耗时：" + ((end - start) / 1000.0) + "s");
//                }
//            }
//        } catch (Exception e) {
//            log.error("读取异常", e);
//        }
//    }
//
//    @Override
//    public void preview(Integer number) {
//        read(null, number, new Function<List<String[]>, Boolean>() {
//            @Override
//            public Boolean apply(List<String[]> valuesList) {
//                for (String[] values : valuesList) {
//                    System.out.println(Arrays.asList(values));
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void count() {
//        final TxtData.Count c = new TxtData.Count();
//        read(null, 10000, new Function<List<String[]>, Boolean>() {
//            @Override
//            public Boolean apply(List<String[]> valuesList) {
//                c.add(valuesList.size());
//                return true;
//            }
//        });
//        System.out.println("CSV File[" + path + "] count:" + c.get());
//    }
//
//    public void write2db(String url, String username, String password, String schema, String[] columns) {
//        try (final JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
//            // 判断表是否存在，如果不存在则创建，存在直接用
//            if (!jdbc.exist(schema)) {
//                // 是否自带id列，默认是不带的
//                boolean idFlag = false;
//                // 创建表
//                StringBuilder columnsStr = new StringBuilder();
//                for (String column : columns) {
//                    if ("id".equalsIgnoreCase(column)) {
//                        idFlag = true;
//                    }
//                    columnsStr.append(",").append(PinYinUtils.pinyin(column)).append(" varchar(255)");
//                }
//                // 自带id列不可用自增
//                String idStr;
//                if (!idFlag) {
//                    idStr = "id int auto_increment primary key";
//                } else {
//                    idStr = "uid int auto_increment primary key";
//                }
//                String ddl = String.format("create table if not exists %s(%s%s)", schema, idStr, columnsStr);
//                int result = jdbc.ddlExecute(ddl);
//                if (result == -1) {
//                    throw new RuntimeException("创建表[" + schema + "]失败");
//                }
//            }
//
//            final ErrorData error = new ErrorData(schema);
//            read(null, 10000, new Function<List<String[]>, Boolean>() {
//                @Override
//                public Boolean apply(List<String[]> lineValuesList) {
//                    List<List<Object>> values = new ArrayList<>();
//                    int columnsNum = columns.length;
//                    for (String[] lineValues : lineValuesList) {
//                        if (null == lineValues || lineValues.length == 0) {
//                            continue;
//                        }
//
//                        if (lineValues.length != columnsNum) {
//                            error.add("[" + String.join(",", lineValues) + "]");
//                            //log.debug("error data:" + str);
//                            continue;
//                        }
//
//                        List<Object> value = new ArrayList<>(columnsNum);
//                        for (int i = 0; i < columnsNum; i++) {
//                            value.add(lineValues[i].trim());
//                        }
//                        values.add(value);
//                    }
//                    log.debug("错误数据数：" + error.total());
//                    jdbc.batchUpdate(schema, columns, values, 1000);
//                    error.write2db(jdbc);
//                    return true;
//                }
//            });
//        }
//    }
//
//    public static void main(String[] args) {
//        String path = "D:\\downloads\\Chrome\\companyinfo.csv";
//
//        CsvData csvData = new CsvData(path, "UTF-8");
//        csvData.preview();
//        csvData.count();
//    }
}
