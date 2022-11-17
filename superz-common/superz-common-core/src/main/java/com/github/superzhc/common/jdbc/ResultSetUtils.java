package com.github.superzhc.common.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 2020年07月16日 superz add
 */
public class ResultSetUtils {
    public static List<Map<String, Object>> Result2ListMap(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < cols_len; i++) {
                    // 2022年5月24日 modify 列名统一小写
                    String cols_name = metaData.getColumnLabel(i + 1).toLowerCase()/*metaData.getColumnName(i + 1)*/;
                    Object cols_value = rs.getObject(cols_name);
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static <T> List<T> Result2ListBean(ResultSet rs, Class<T> beanClass) {
        List<T> list = new ArrayList<>();
        try {
            boolean emptyConstructor = false;
            Constructor[] constructors = beanClass.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    emptyConstructor = true;
                    break;
                }
            }
            if (!emptyConstructor)
                throw new RuntimeException("无空构造函数，Map无法转" + beanClass.getName());

            Field[] allFields = beanClass.getDeclaredFields();

            // 注意：某些数据库只支持读一次 metaData，不重复读操作
            ResultSetMetaData metaData = rs.getMetaData();
            int cols_len = metaData.getColumnCount();

            Map<String, Field> colNameFieldMap = new HashMap<>(Math.min(allFields.length, cols_len));

            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);

                // 支持驼峰命名方式 TODO

                for (Field field : allFields) {
                    int mod = field.getModifiers();
                    // 静态变量不做处理，一般Bean中不存在静态变量
                    if (Modifier.isStatic(mod))
                        continue;

                    if (field.getName().equals(cols_name)) {
                        field.setAccessible(true);
                        colNameFieldMap.put(cols_name, field);
                        break;
                    }

                }
            }

            while (rs.next()) {
                T obj = beanClass.newInstance();

                for (Map.Entry<String, Field> item : colNameFieldMap.entrySet()) {
                    String cols_name = item.getKey();
                    Object cols_value = rs.getObject(cols_name);
                    if (null == cols_value) {
                        continue;
                    }
                    item.getValue().set(obj, cols_value);
                }
                list.add(obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void print(ResultSet rs) throws SQLException {
        print(rs, 20);
    }

    /**
     * 打印数据
     *
     * @param rs
     * @param num 预览的数据条数
     * @throws SQLException
     */
    public static void print(ResultSet rs, int num) throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        // 获取列数
        int ColumnCount = resultSetMetaData.getColumnCount();
        // 保存当前列最大长度的数组
        int[] columnMaxLengths = new int[ColumnCount];
        String[] columnNames = new String[ColumnCount];
        // 初始化列的长度
        for (int i = 0; i < ColumnCount; i++) {
            String columnName = resultSetMetaData.getColumnName(i + 1).toLowerCase();
            columnMaxLengths[i] = length(columnName);
            columnNames[i] = columnName;
        }

        // 缓存结果集
        ArrayList<String[]> results = new ArrayList<>();
        // 按行遍历
        int cur = 0;
        while (rs.next() && (num == -1 || num > cur++)) {
            // 保存当前行所有列
            String[] columnStr = new String[ColumnCount];
            // 获取属性值.
            for (int i = 0; i < ColumnCount; i++) {
                // 获取一列
                columnStr[i] = rs.getString(i + 1);
                // 计算当前列的最大长度
                columnMaxLengths[i] = Math.max(columnMaxLengths[i], (columnStr[i] == null) ? 0 : length(columnStr[i]));
            }
            // 缓存这一行.
            results.add(columnStr);
        }
        printSeparator(columnMaxLengths);
        // printColumnName(resultSetMetaData, columnMaxLengths);
        printColumnName(columnNames, columnMaxLengths);
        printSeparator(columnMaxLengths);
        // 遍历集合输出结果
        Iterator<String[]> iterator = results.iterator();
        String[] columnStr;
        while (iterator.hasNext()) {
            columnStr = iterator.next();
            for (int i = 0; i < ColumnCount; i++) {
                // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s", columnStr[i]);
                // 2020年11月4日 左对齐使用 %-10s，右对齐 %10s；修改为左对齐
                // System.out.printf("|%-" + columnMaxLengths[i] + "s", columnStr[i]);
                // 2022年6月27日 直接使用printf还是对不齐输出
                System.out.print("|");
                System.out.print(rightPad(columnStr[i], columnMaxLengths[i]));
            }
            System.out.println("|");
        }
        printSeparator(columnMaxLengths);
    }

    /**
     * 输出列名.
     *
     * @param resultSetMetaData 结果集的元数据对象.
     * @param columnMaxLengths  每一列最大长度的字符串的长度.
     * @throws SQLException
     */
    @Deprecated
    private static void printColumnName(ResultSetMetaData resultSetMetaData, int[] columnMaxLengths)
            throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s",
            // resultSetMetaData.getColumnName(i + 1));
            // 2020年11月4日 修改为左对齐
            // System.out.printf("|%-" + columnMaxLengths[i] + "s", resultSetMetaData.getColumnName(i + 1));
            // 2022年6月27日 直接使用printf还是对不齐输出
            System.out.print("|");
            System.out.print(rightPad(resultSetMetaData.getColumnName(i + 1), columnMaxLengths[i]));
        }
        System.out.println("|");
    }

    /**
     * 2022年5月20日 modify 输出列名
     *
     * @param columnNames      列名的集合
     * @param columnMaxLengths 每一列最大长度的字符串的长度
     */
    private static void printColumnName(String[] columnNames, int[] columnMaxLengths) {
        int columnCount = columnNames.length;
        for (int i = 0; i < columnCount; i++) {
            // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s",
            // resultSetMetaData.getColumnName(i + 1));
            // 2020年11月4日 修改为左对齐
            // System.out.printf("|%-" + columnMaxLengths[i] + "s", columnNames[i]);
            // 2022年6月27日 直接使用printf还是对不齐输出
            System.out.print("|");
            System.out.print(rightPad(columnNames[i], columnMaxLengths[i]));
        }
        System.out.println("|");
    }

    /**
     * 输出分隔符.
     *
     * @param columnMaxLengths 保存结果集中每一列的最长的字符串的长度.
     */
    private static void printSeparator(int[] columnMaxLengths) {
        for (int i = 0; i < columnMaxLengths.length; i++) {
            System.out.print("+");
            for (int j = 0; j < columnMaxLengths[i] + 1; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    private static String rightPad(String str, int maxLength) {
        int len = length(str);
        for (int i = 0; i < ((maxLength - len) + 1); i++) {
            str += " ";
        }
        return null == str ? "" : str;
    }

    private static int length(String str) {
        if (null == str) {
            return 0;
        }

        int valueLength = 0;
        // String chinese = "[\u0391-\uFFE5]";//匹配中文字符的正则表达式： [\u4e00-\u9fa5]
        // String doubleChar = "[^\\x00-\\xff]";// 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
        String doubleChar = "[\u1100-\u115F\u2E80-\uA4CF\uAC00-\uD7A3\uF900-\uFAFF\uFE10-\uFE19\uFE30-\uFE6F\uFF00-\uFF60\uFFE0-\uFFE6]";
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
