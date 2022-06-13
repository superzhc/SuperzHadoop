package com.github.superzhc.common.file.text;

import com.github.superzhc.common.file.setting.FileReaderSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/13 9:31
 **/
public class TextReader {
    private static final Logger log = LoggerFactory.getLogger(TextReader.class);

    public static class TextReaderSetting extends FileReaderSetting<String> {
        @Override
        public TextReaderSetting setPath(String path) {
            super.setPath(path);
            return this;
        }

        @Override
        public TextReaderSetting setCharset(String charset) {
            super.setCharset(charset);
            return this;
        }

        @Override
        public TextReaderSetting setBatchSize(Integer batchSize) {
            super.setBatchSize(batchSize);
            return this;
        }

        @Override
        public TextReaderSetting setDataFunction(Function<List<String>, Object> dataFunction) {
            super.setDataFunction(dataFunction);
            return this;
        }
    }

    public static void preview(String path) {
        preview(path, null, 20);
    }

    public static void preview(String path, Integer num) {
        preview(path, null, num);
    }

    public static void preview(String path, String charset, Integer num) {
        TextReaderSetting setting = new TextReaderSetting();
        setting.setPath(path);
        if (null != charset && charset.trim().length() > 0) {
            setting.setCharset(charset);
        }
        setting.setBatchSize(num);

        setting.setDataFunction(new Function<List<String>, Object>() {
            @Override
            public Object apply(List<String> strings) {
                for (String str : strings) {
                    System.out.println(str);
                }
                return false;
            }
        });

        read(setting);
    }

    public static List<String> read(String path) {
        return read(path, null);
    }

    public static List<String> read(String path, String charset) {
        TextReaderSetting setting = new TextReaderSetting();
        setting.setPath(path);
        if (null != charset && charset.trim().length() > 0) {
            setting.setCharset(charset);
        }
        setting.setBatchSize(1000);

        final List<String> content = new ArrayList<>();
        setting.setDataFunction(new Function<List<String>, Object>() {
            @Override
            public Object apply(List<String> strings) {
                content.addAll(strings);
                return null;
            }
        });

        read(setting);

        return content;
    }

    public static <T> List<T> read(TextReaderSetting setting) {
        List<T> ret = null;
        try (InputStream inputStream = new FileInputStream(setting.getPath()/*new File(path)*/)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(setting.getCharset()))) {
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    log.debug("文件开始读取...");
                    long start = System.currentTimeMillis();
                    long total = 0L;
                    long currentLineNum = 0;
                    ret = new ArrayList<>();

                    String line = null;
                    List<String> lines = new ArrayList<>(setting.getBatchSize());
                    int currentBatchLineNum = 0;
                    while ((line = reader.readLine()) != null) {
                        total++;
                        currentLineNum++;
                        currentBatchLineNum++;

                        lines.add(line);

                        if (currentBatchLineNum >= setting.getBatchSize()) {
                            if (log.isDebugEnabled() && setting.getBatchSize() > 1) {
                                log.debug("文件处理中，行号：[" + (currentLineNum - currentBatchLineNum + 1) + "~" + currentBatchLineNum + "]");
                            }

                            Object result = setting.getDataFunction().apply(lines);
                            if (null != result) {
                                ret.add((T) result);
                            }
                            currentBatchLineNum = 0;
                            lines.clear();

                            if (result instanceof Boolean) {
                                Boolean flag = (Boolean) result;
                                if (!flag) {
                                    total = 0L;
                                    break;
                                }
                            }
                        }
                    }

                    if (currentBatchLineNum > 0) {
                        if (log.isDebugEnabled() && setting.getBatchSize() > 1) {
                            log.debug("文件处理中，行号：[" + (currentLineNum - currentBatchLineNum + 1) + "~" + currentBatchLineNum + "]");
                        }

                        Object result = setting.getDataFunction().apply(lines);
                        log.debug("文件处理中，处理返回结果：" + result);
                        if (null != result) {
                            ret.add((T) result);
                        }
                    }

                    long end = System.currentTimeMillis();
                    log.debug((total > 0 ? "文件处理完成，文件总行数：" + total + "，" : "") + "文件处理总耗时：" + ((end - start) / 1000.0) + "s");

                    return ret;
                }
            }
        } catch (Exception e) {
            log.error("读取异常", e);
        }
        return ret;
    }

    public static void main(String[] args) {
        String path = "E:\\downloads\\wordcount.txt";

        preview(path);

        List<String> content = read(path);
        String str = String.join("@", content);
        System.out.println(str);
    }
}
