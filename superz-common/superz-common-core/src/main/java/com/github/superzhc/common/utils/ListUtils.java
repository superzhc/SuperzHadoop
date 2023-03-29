package com.github.superzhc.common.utils;

import java.util.*;

/**
 * @author superz
 * @create 2021/4/8 15:49
 */
public class ListUtils {
    public static String list2String(List<?> lst) {
        return list2String(lst, " ");
    }

    public static String list2String(List<?> lst, String separator) {
        StringBuilder sb = new StringBuilder();
        if (null == lst || lst.size() == 0) {
            return null;
        }

        int size = lst.size();
        for (int i = 0, len = size - 1; i < len; i++) {
            sb.append(lst.get(i)).append(separator);
        }
        sb.append(lst.get(size - 1));
        return sb.toString();
    }

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public static <T> void show(String header, T... data) {
        System.out.println(print(header, data));
    }

    public static <T> void show(String header, List<T> data) {
        System.out.println(print(header, data));
    }

    public static <T> String print(String header, T... data) {
        return print(header, (null == data || data.length == 0) ? new ArrayList<T>() : Arrays.asList(data));
    }

    public static <T> String print(String header, List<T> data) {
        List<List<T>> dataList = new ArrayList<>();
        if (null != data && data.size() > 0) {
            for (T item : data) {
                List<T> itemList = new ArrayList<>();
                itemList.add(item);

                dataList.add(itemList);
            }
        }
        return print(Collections.singletonList(header), dataList);
    }

    public static <T> String print(String[] headers, T[]... data) {
        if (null == data) {
            return print(Arrays.asList(headers), new ArrayList<>());
        }
        int dataSize = data.length;
        List<List<T>> newData = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            newData.add(Arrays.asList(data[i]));
        }

        return print(Arrays.asList(headers), newData);
    }

    public static <T> String print(String[] headers, List<T[]> data) {
        int dataSize = data.size();
        List<List<T>> newData = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            newData.add(Arrays.asList(data.get(i)));
        }

        return print(Arrays.asList(headers), newData);
    }

    public static <T> String print(List<String> headers, List<List<T>> data) {
        if (null == data) {
            return "暂无数据";
        }

        if (null == headers) {
            int headerLength = 0;
            for (List<T> item : data) {
                headerLength = Math.max(headerLength, item.size());
            }

            headers = new ArrayList<>();
            for (int i = 0; i < headerLength; i++) {
                headers.add(String.format("C%d", i));
            }
        }

        int columnSize = headers.size();
        int[] columnMaxLengths = new int[columnSize];
        String[] headerRow = new String[columnSize];
        for (int i = 0; i < columnSize; i++) {
            columnMaxLengths[i] = stringLength(headers.get(i));
            headerRow[i] = headers.get(i);
        }

        List<String[]> rows = new ArrayList<>();
        for (int j = 0, rowNum = data.size(); j < rowNum; j++) {
            List<T> item = data.get(j);
            String[] row = new String[columnSize];
            for (int m = 0, len = Math.min(columnSize, item.size()); m < len; m++) {
                String value = String.valueOf(item.get(m));
                row[m] = value;
                columnMaxLengths[m] = Math.max(columnMaxLengths[m], (null == value ? 0 : stringLength(value)));
            }
            rows.add(row);
        }

        StringBuilder result = new StringBuilder();
        result.append(printSeparator(columnMaxLengths)).append("\n");
        result.append(printRow(headerRow, columnMaxLengths)).append("\n");
        result.append(printSeparator(columnMaxLengths)).append("\n");
        for (String[] row : rows) {
            result.append(printRow(row, columnMaxLengths)).append("\n");
        }
        result.append(printSeparator(columnMaxLengths)).append("\n");

        result.append("展示数据条数：").append(data.size()).append("\n");

        return result.toString();
    }

    private static String printRow(String[] row, int[] columnMaxLengths) {
        StringBuilder sb = new StringBuilder();
        int columnCount = row.length;
        for (int i = 0; i < columnCount; i++) {
            sb.append("|");
            sb.append(rightPad(row[i], columnMaxLengths[i]));
        }
        sb.append("|");
        return sb.toString();
    }

    private static String printSeparator(int[] columnMaxLengths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnMaxLengths.length; i++) {
            sb.append("+");
            for (int j = 0; j < columnMaxLengths[i] + 1; j++) {
                sb.append("-");
            }
        }
        sb.append("+");
        return sb.toString();
    }

    private static String rightPad(String str, int maxLength) {
        int len = stringLength(str);
        StringBuilder sb = new StringBuilder(null == str ? "" : str);
        for (int i = 0; i < ((maxLength - len) + 1); i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static int stringLength(String str) {
        if (null == str) {
            return 0;
        }

        // String chinese = "[\u0391-\uFFE5]";//匹配中文字符的正则表达式： [\u4e00-\u9fa5]
        // String doubleChar = "[^\\x00-\\xff]";// 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
        String doubleChar = "[" +
                "\u1100-\u115F" +
                "\u2E80-\uA4CF" +
                "\uAC00-\uD7A3" +
                "\uF900-\uFAFF" +
                "\uFE10-\uFE19" +
                "\uFE30-\uFE6F" +
                "\uFF00-\uFF60" +
                "\uFFE0-\uFFE6" +
                "]";

        int valueLength = 0;
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < str.length(); i++) {
            /* 获取一个字符 */
            String temp = str.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(doubleChar)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
