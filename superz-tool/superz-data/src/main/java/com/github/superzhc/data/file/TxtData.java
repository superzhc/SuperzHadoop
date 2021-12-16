package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

    private InputStream inputStream;

    public TxtData(String path) {
        this(path, DEFAULT_CHARSET, DEFAULT_HEADERS);
    }

    public TxtData(String path, String charset) {
        this(path, charset, DEFAULT_HEADERS);
    }

    public TxtData(String path, String charset, Integer headers) {
        this.path = path;
        this.charset = charset;
        this.headers = headers;
        init();
    }

    private void init() {
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void read(Function<List<String>, Void> headersFunction, Integer linesNum, Function<List<String>, Boolean> linesFunction) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(charset))) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                log.debug("文件开始读取...");
                long start = System.currentTimeMillis();
                long total = 0L;

                String str;

                /* 处理文件头 */
                if (headers > 0 && null != headersFunction) {
                    List<String> headerLines = new ArrayList<>(headers);
                    Integer headerCursor = headers;
                    while ((str = reader.readLine()) != null && headerCursor > 0) {
                        headerLines.add(str);
                        headerCursor--;
                    }
                    headersFunction.apply(headerLines);
                }

                /* 处理文件内容 */
                List<String> lines = new ArrayList<>(linesNum);
                int currentLinesNum = 0;
                while ((str = reader.readLine()) != null) {
                    lines.add(str);
                    currentLinesNum++;
                    total++;

                    if (currentLinesNum >= linesNum) {
                        Boolean flag = linesFunction.apply(lines);
                        currentLinesNum = 0;
                        lines.clear();

                        if (!flag) {
                            total = 0L;
                            break;
                        }
                    }
                }
                if (currentLinesNum > 0) {
                    linesFunction.apply(lines);
                }

                long end = System.currentTimeMillis();
                log.debug((total > 0 ? "文件总行数：" + total + "，" : "") + "文件处理总耗时：" + ((end - start) / 1000.0) + "s");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preview(Integer number) {
        read(null, number, new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                for (String str : strings) {
                    System.out.println(str);
                }
                return false;
            }
        });
    }

    public void write2db(String url, String username, String password, String schema, String[] columns) {
        try (final JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            read(null, 10000, new Function<List<String>, Boolean>() {
                @Override
                public Boolean apply(List<String> strings) {
                    List<List<Object>> values = new ArrayList<>();
                    for (String str : strings) {
                        String[] arr = str.split("\t");
                        if (arr.length < 2) {
                            log.debug("error data:" + str);
                            continue;
                        }
                        List<Object> value = new ArrayList<>();
                        value.add(arr[0]);
                        value.add(arr[1]);
                        values.add(value);
                    }
                    jdbc.batchUpdate(schema, columns, values, 1000);
                    return true;
                }
            });
        }
    }

    public static void main(String[] args) {
        String path = "D:\\download\\chrome\\xh-2.txt";

        String url = "jdbc:mysql://localhost:3306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        TxtData txtData = new TxtData(path);
        //txtData.preview();
        txtData.write2db(url, username, password, "renren", new String[]{"email", "note"});
    }
}
