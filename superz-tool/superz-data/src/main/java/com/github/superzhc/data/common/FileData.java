package com.github.superzhc.data.common;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2021/12/13 20:02
 */
public class FileData {
    private static final Integer DEFAULT_NUMBER = 20;

    public void show(String path) {
        show(path, DEFAULT_NUMBER);
    }

    public void show(String path, final Integer number) {
        print(path, number);
    }

    public void print(String path) {
        print(path, DEFAULT_NUMBER);
    }

    public void print(String path, final Integer number) {
        file(path, number, new Function<List<String>, Boolean>() {
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
        return take(path, DEFAULT_NUMBER);
    }

    public List<String> take(String path, final Integer number) {
        final List<String> result = new ArrayList<>();
        file(path, number, new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                result.addAll(strings);
                return result.size() < number;
            }
        });
        return result;
    }

    protected void file(String path, Integer linesNum, Function<List<String>, Boolean> linesFunction) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            read(fileInputStream, linesNum, linesFunction);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    protected void read(InputStream in, Function<String, Boolean> lineFunction) {
        read(in, 1, new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                return lineFunction.apply(strings.get(0));
            }
        });
    }

    protected void read(InputStream in, Integer linesNum, Function<List<String>, Boolean> linesFunction) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"))) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                List<String> lines = new ArrayList<>(linesNum);
                Integer currentLinesNum = 0;

                String str;
                while ((str = reader.readLine()) != null) {
                    lines.add(str);
                    currentLinesNum++;

                    if (currentLinesNum == linesNum) {
                        Boolean flag = linesFunction.apply(lines);
                        currentLinesNum = 0;
                        lines.clear();

                        if (!flag) {
                            break;
                        }
                    }
                }

                if (currentLinesNum > 0) {
                    linesFunction.apply(lines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
