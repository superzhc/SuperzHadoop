package com.github.superzhc.fund.tablesaw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2022/4/2 15:55
 **/
public class ColumnUtils {
    public static final String DEFAULT_SYMBOL = "_";
    public static final String DEFAULT_TRANSFORM_PREFIX = "C";

    /**
     * python 的 pandas 不需要的列名可直接使用下划线而无需具体的命名，但 tablesaw 不支持列名重复，这块做一个转换将所有下划线都换成具体的列名的工具
     *
     * @param names
     * @return
     */
    public static List<String> transform(String... names) {
        int cursor = 1;
        for (int i = 0, len = names.length; i < len; i++) {
            if (DEFAULT_SYMBOL.equals(names[i])) {
                names[i] = String.format("%s%d", DEFAULT_TRANSFORM_PREFIX, cursor++);
            }
        }
        return Arrays.asList(names);
    }

    public static String[] efficientColumnNames(String... names) {
        List<String> columnNames = new ArrayList<>();
        for (String name : names) {
            if (!DEFAULT_SYMBOL.equals(name)) {
                columnNames.add(name);
            }
        }

        String[] columnNameArr = new String[columnNames.size()];
        columnNameArr = columnNames.toArray(columnNameArr);
        return columnNameArr;
    }

    public static void main(String[] args) {
        System.out.println(transform("a", "b", "_", "_", "_", "_", "d"));
    }
}
