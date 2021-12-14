package com.github.superzhc.data.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/12/14 13:55
 */
public abstract class NFileData {
    private static final Logger log = LoggerFactory.getLogger(NFileData.class);

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Integer NO_HEADER = 0;

    protected Integer batchSize;

    public NFileData() {
        this(1);
    }

    public NFileData(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public final void read(String path) {
        read(path, DEFAULT_CHARSET, NO_HEADER);
    }

    public final void read(String path, Integer headerNumber) {
        read(path, DEFAULT_CHARSET, headerNumber);
    }

    public final void read(String path, String charset) {
        read(path, charset, NO_HEADER);
    }

    public final void read(String path, String charset, Integer headerNumber) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            read(fileInputStream, charset, headerNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void read(InputStream in) {
        read(in, DEFAULT_CHARSET, NO_HEADER);
    }

    public final void read(InputStream in, Integer headerNumber) {
        read(in, DEFAULT_CHARSET, headerNumber);
    }

    public final void read(InputStream in, String charset) {
        read(in, charset, NO_HEADER);
    }

    public final void read(InputStream in, String charset, Integer headerNumber) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName(charset))) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {

                log.debug("文件开始读取...");
                long start = System.currentTimeMillis();
                long total = 0L;

                String str;

                /* 处理文件头 */
                if (headerNumber > 0) {
                    List<String> headerLines = new ArrayList<>(headerNumber);
                    Integer headerCursor = headerNumber;
                    while ((str = reader.readLine()) != null && headerCursor > 0) {
                        headerLines.add(str);
                        headerCursor--;
                    }
                    dealHeaderLines(headerLines);
                }

                if (batchSize < 2) {
                    /* 逐行处理文件内容 */
                    while ((str = reader.readLine()) != null) {
                        boolean flag = dealLine(str);
                        total++;
                        if (!flag) {
                            total = 0L;
                            break;
                        }
                    }
                } else {
                    List<String> lines = new ArrayList<>(batchSize);
                    int currentLinesNum = 0;
                    while ((str = reader.readLine()) != null) {
                        lines.add(str);
                        currentLinesNum++;
                        total++;

                        if (currentLinesNum >= batchSize) {
                            boolean flag = dealLines(lines);
                            currentLinesNum = 0;
                            lines.clear();

                            if (!flag) {
                                total = 0L;
                                break;
                            }
                        }
                    }

                    if (currentLinesNum > 0) {
                        dealLines(lines);
                    }
                }


                long end = System.currentTimeMillis();
                log.debug((total > 0 ? "文件总行数：" + total + "，" : "") + "文件处理总耗时：" + ((end - start) / 1000.0) + "s");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void dealHeaderLines(List<String> headerLines) {
    }

    /**
     * 默认遍历所有行，并将每行还是有每个处理行数处理
     *
     * @param strings
     * @return
     */
    protected boolean dealLines(List<String> strings) {
        boolean result = true;
        for (String str : strings) {
            // 2021年12月14日 短路逻辑会造成dealLine不执行
            //result = result && dealLine(str);
            result = dealLine(str) && result;
        }
        return result;
    }

    protected abstract boolean dealLine(String str);
}
