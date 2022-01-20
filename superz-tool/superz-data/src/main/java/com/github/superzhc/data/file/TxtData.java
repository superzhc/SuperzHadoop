package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.FileData;
import com.github.superzhc.data.utils.PinYinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2021/12/15 1:21
 */
public class TxtData implements FileData {
    private static final Logger log = LoggerFactory.getLogger(TxtData.class);

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Integer DEFAULT_HEADERS = 0;

    private String path;
    private String charset;
    private Integer headers;

    //private InputStream inputStream;

    public TxtData(String path) {
        this(path, DEFAULT_CHARSET, DEFAULT_HEADERS);
    }

    public TxtData(String path, Integer headers) {
        this(path, DEFAULT_CHARSET, headers);
    }

    public TxtData(String path, String charset) {
        this(path, charset, DEFAULT_HEADERS);
    }

    public TxtData(String path, String charset, Integer headers) {
        this.path = path;
        this.charset = charset;
        this.headers = headers;
        //init();
    }

    /**
     * 不能在这初始化，不然的话流只能读一次报错
     */
//    private void init() {
//        try {
//            inputStream = new FileInputStream(new File(path));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Override
    public void read(FileReadSetting settings) {

        // 默认settings中配置的头标题行数优先级最高
        if (0 == settings.getHeaders()) {
            settings.setHeaders(headers);
        }

        try (InputStream inputStream = new FileInputStream(new File(path))) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(charset))) {
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    log.debug("文件开始读取...");
                    long start = System.currentTimeMillis();
                    long total = 0L;

                    String str;

                    Long currentLines = 0L;
                    /* 处理文件头 */
                    if (settings.getHeaders() > 0) {
                        List<Object> headerLines = new ArrayList<>(headers);
                        Integer headerCursor = headers;
                        while (headerCursor > 0 && (str = reader.readLine()) != null) {
                            currentLines++;
                            headerLines.add(str);
                            headerCursor--;
                        }

                        if (null != settings.getHeaderFunction()) {
                            settings.getHeaderFunction().apply(headerLines);
                        } else {
                            log.debug("untreated header data:" + headerLines);
                        }
                    }

                    /* 处理文件内容 */
                    // 批处理逻辑
                    if (settings.getBatchSize() > 1) {
                        List<Object> lines = new ArrayList<>(settings.getBatchSize());
                        int currentLinesNum = 0;
                        while ((str = reader.readLine()) != null) {
                            currentLines++;
                            lines.add(str);
                            currentLinesNum++;
                            total++;

                            if (currentLinesNum >= settings.getBatchSize()) {
                                log.debug("文件处理中，行号：[" + (currentLines - currentLinesNum + 1) + "~" + currentLines + "]");
                                Boolean flag = settings.getLinesFunction().apply(lines);
                                currentLinesNum = 0;
                                lines.clear();

                                if (!flag) {
                                    total = 0L;
                                    break;
                                }
                            }
                        }
                        if (currentLinesNum > 0) {
                            log.debug("文件处理中，行号：" + (currentLines - currentLinesNum + 1) + "~" + currentLines);
                            settings.getLinesFunction().apply(lines);
                        }
                    }
                    // 逐行处理
                    else {
                        while ((str = reader.readLine()) != null) {
                            total++;
                            Boolean flag = settings.getLinesFunction().apply(Collections.singletonList(str));
                            if (!flag) {
                                total = 0L;
                                break;
                            }
                        }
                    }

                    long end = System.currentTimeMillis();
                    log.debug((total > 0 ? "文件处理完成，文件总行数：" + total + "，" : "") + "文件处理总耗时：" + ((end - start) / 1000.0) + "s");
                }
            }
        } catch (Exception e) {
            log.error("读取异常", e);
        }
    }

    private void read(Function<List<Object>, Void> headersFunction, Integer linesNum, Function<List<Object>, Boolean> linesFunction) {
        FileReadSetting settings = new FileReadSetting(headers, headersFunction, linesNum, linesFunction);
        read(settings);
    }

    @Override
    public void preview(final Integer number) {
        read(null, number, new Function<List<Object>, Boolean>() {
            @Override
            public Boolean apply(List<Object> strings) {
                for (Object str : strings) {
                    System.out.println(str);
                }
                return false;
            }
        });
    }

    @Override
    public void count() {
        final Count c = new Count();
        read(null, 1, new Function<List<Object>, Boolean>() {
            @Override
            public Boolean apply(List<Object> strings) {
                c.add(null == strings ? 0 : strings.size());
                return true;
            }
        });
        System.out.println("File[" + path + "] count:" + c.get());
    }

    public static class Count {
        private Integer number = 0;

        public void add(Integer i) {
            number += i;
        }

        public Integer get() {
            return number;
        }
    }

    public void write2db(String url, String username, String password, String schema, String[] columns) {
        write2db(url, username, password, schema, columns, "\t");
    }

    public void write2db(String url, String username, String password, String schema, String[] columns, String separator) {
        try (final JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            // 判断表是否存在，如果不存在则创建，存在直接用
            if (!jdbc.exist(schema)) {
                // 是否自带id列，默认是不带的
                boolean idFlag = false;
                // 创建表
                StringBuilder columnsStr = new StringBuilder();
                for (String column : columns) {
                    if ("id".equalsIgnoreCase(column)) {
                        idFlag = true;
                    }

                    String type = "varchar(255)";
                    if ("address".equalsIgnoreCase(column) || "addr".equalsIgnoreCase(column)) {
                        type = "text";
                    }

                    columnsStr.append(",").append(PinYinUtils.pinyin(column)).append(" ").append(type);
                }
                // 自带id列不可用自增
                String idStr;
                if (!idFlag) {
                    idStr = "id int auto_increment primary key";
                } else {
                    idStr = "uid int auto_increment primary key";
                }
                // create table if not exists %s(%s%s)ENGINE=MyISAM
                String ddl = String.format("create table if not exists %s(%s%s) ENGINE=MyISAM DEFAULT CHARSET = utf8mb4", schema, idStr, columnsStr);
                int result = jdbc.ddlExecute(ddl);
                if (result == -1) {
                    throw new RuntimeException("创建表[" + schema + "]失败");
                }
            }

            final ErrorData error = new ErrorData(schema);
            read(null, 10000, new Function<List<Object>, Boolean>() {
                @Override
                public Boolean apply(List<Object> strings) {
                    List<List<Object>> values = new ArrayList<>();
                    int columnsNum = columns.length;
                    for (Object obj : strings) {
                        String str = (String) obj;
                        if (null == str || str.trim().length() == 0) {
                            continue;
                        }

                        // str的结尾为空再不设置limit的情况下会被删掉，因此设置-1保证结尾即使为空也不会被删除
                        String[] arr = str.split(separator, -1);
                        if (arr.length != columnsNum) {
                            error.add(str);
                            //log.debug("error data:" + str);
                            continue;
                        }

                        List<Object> value = new ArrayList<>(columnsNum);
                        for (int i = 0; i < columnsNum; i++) {
                            String s = arr[i].trim();
                            if (s.length() > 255) {
                                error.add(str);
                                break;
                            }
                            value.add(s);
                        }

                        if (value.size() == arr.length) {
                            values.add(value);
                        }
                    }
                    log.debug("错误数据数：" + error.total());
                    jdbc.batchUpdate(schema, columns, values, 1000);
                    error.write2db(jdbc);
                    return true;
                }
            });
        }
    }

    public static void main(String[] args) {
        String path = "D:\\downloads\\Chrome\\";
        String fileName = "xxx.txt";
        path = path + fileName;

        //path = "D:\\downloads\\Chrome\\users.json";

        path = "D:\\downloads\\Chrome\\companyinfo.csv";

        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        TxtData txtData = new TxtData(path, 1);
        txtData.preview();
        // txtData.count();
        txtData.write2db(url, username, password, "company_info", "email,tel,entity,regno,legal,oldname,taxno,licenseno,lat,lon".split(","), ",");
    }
}
