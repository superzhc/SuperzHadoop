package com.github.superzhc.data.common;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 该方法扩展性有点差
 *
 * @author superz
 * @create 2021/12/13 20:02
 */
@Deprecated
public class OldFileData {
    private static final Logger log = LoggerFactory.getLogger(OldFileData.class);

    private static final Integer DEFAULT_NUMBER = 20;
    private static final String DEFAULT_CHARSET = "UTF-8";

    public void show(String path) {
        show(path, DEFAULT_NUMBER, DEFAULT_CHARSET);
    }

    public void show(String path, String charset) {
        show(path, DEFAULT_NUMBER, charset);
    }

    public void show(String path, final Integer number, String charset) {
        print(path, number, charset);
    }

    public void print(String path) {
        print(path, DEFAULT_NUMBER, DEFAULT_CHARSET);
    }

    public void print(String path, String charset) {
        print(path, DEFAULT_NUMBER, charset);
    }

    public void print(String path, final Integer number, String charset) {
        file(path, number, charset, new Function<List<String>, Boolean>() {
            private Integer cursor = number;

            @Override
            public Boolean apply(List<String> strings) {
                for (String s : strings) {
                    System.out.println(s);
                    cursor--;
                }

                return cursor > 0;
            }
        });
    }

    public List<String> take(String path) {
        return take(path, DEFAULT_NUMBER, DEFAULT_CHARSET);
    }

    public List<String> take(String path, String charset) {
        return take(path, DEFAULT_NUMBER, charset);
    }

    public List<String> take(String path, final Integer number, String charset) {
        final List<String> result = new ArrayList<>();
        file(path, number, charset, new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                result.addAll(strings);
                return result.size() < number;
            }
        });
        return result;
    }

    public void file(String path, Integer linesNum, Function<List<String>, Boolean> linesFunction) {
        file(path, linesNum, DEFAULT_CHARSET, linesFunction);
    }

    public void file(String path, Integer linesNum, String charset, Function<List<String>, Boolean> linesFunction) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            read(fileInputStream, linesNum, charset, linesFunction);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    protected void read(InputStream in, String charset, Function<String, Boolean> lineFunction) {
        read(in, 1, charset, new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                return lineFunction.apply(strings.get(0));
            }
        });
    }

    protected void read(InputStream in, Integer linesNum, String charset, Function<List<String>, Boolean> linesFunction) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName(charset))) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                List<String> lines = new ArrayList<>(linesNum);
                int currentLinesNum = 0;

                log.debug("文件开始读取...");
                long start = System.currentTimeMillis();
                long total = 0L;

                String str;
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

    public static void main(String[] args) {
        OldFileData fd = new OldFileData();
        //fd.show("D:\\downloads\\tg\\178.comDataBase.txt");

        /* 不合法数据 */
        List<String> unlawfulData = new ArrayList<>();

        final String table = "user_info";
        final String[] columns = {"account", "password", "type"};
        final JdbcHelper jdbc = new JdbcHelper("jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8", "root", "123456");

        fd.file("D:\\downloads\\tg\\imgur.Com DataBase .txt", 10000, new Function<List<String>, Boolean>() {
            private String separator = ":";

            @Override
            public Boolean apply(List<String> strings) {
                List<List<Object>> values = new ArrayList<>();

                for (String s : strings) {
                    if (null == s || s.trim().length() == 0) {
                        continue;
                    }

                    String[] value = s.split(separator);

                    if (value.length < 2) {
                        unlawfulData.add(s);
                        continue;
                    } else if (value.length == 2) {
                        List<Object> item = new ArrayList<>();
                        item.add(value[0]);
                        item.add(value[1]);
                        item.add("imgur.com");
                        values.add(item);
                    } else {
                        List<Object> item = new ArrayList<>();
                        item.add(value[0]);
                        item.add(s.substring(s.indexOf(separator) + 1));
                        item.add("imgur.com");
                        values.add(item);
                    }
                }

                jdbc.batchUpdate(table, columns, values, 1000);

                return true;
            }
        });

        System.out.println("不合法数据条数：" + unlawfulData.size());
        for (String s : unlawfulData) {
            System.out.println(s);
        }
    }
}
