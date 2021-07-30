package com.github.superzhc.xxljob.util.db;

import com.github.superzhc.xxljob.util.BeanUtils;
import com.github.superzhc.xxljob.util.StringUtils;

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
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = rs.getObject(cols_name);
                    // null值不做处理
                    // if (null == cols_value) {
                    // cols_value = "";
                    // }
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
            ResultSetMetaData metaData = rs.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = rs.getObject(cols_name);
                    // null值不做处理
                    // if (null == cols_value) {
                    // cols_value = "";
                    // }
                    map.put(cols_name, cols_value);
                }
                list.add(BeanUtils.mapToBean(map, beanClass));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Deprecated
    public static void print(ResultSet rs) throws SQLException {
        System.out.print(format(rs, 20));
    }

    /**
     * 格式化所有数据
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static String format(ResultSet rs) throws SQLException {
        return format(rs, 20);
    }

    /**
     * 打印数据
     *
     * @param rs
     * @param num 预览的数据条数
     * @throws SQLException
     */
    public static String format(ResultSet rs, int num) throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        // 获取列数
        int ColumnCount = resultSetMetaData.getColumnCount();
        // 保存当前列最大长度的数组
        int[] columnMaxLengths = new int[ColumnCount];
        // 初始化列的长度
        for (int i = 0; i < ColumnCount; i++) {
            columnMaxLengths[i] = StringUtils.length(resultSetMetaData.getColumnName(i + 1));
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
                columnMaxLengths[i] = Math.max(columnMaxLengths[i],
                        (columnStr[i] == null) ? 0 : StringUtils.length(columnStr[i]));
            }
            // 缓存这一行.
            results.add(columnStr);
        }
        sb.append(formatSeparator(columnMaxLengths));
        sb.append(formatColumnName(resultSetMetaData, columnMaxLengths));
        sb.append(formatSeparator(columnMaxLengths));
        // 遍历集合输出结果
        Iterator<String[]> iterator = results.iterator();
        String[] columnStr;
        while (iterator.hasNext()) {
            columnStr = iterator.next();
            for (int i = 0; i < ColumnCount; i++) {
                // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s", columnStr[i]);
                // 2020年11月4日 左对齐使用 %-10s，右对齐 %10s；修改为左对齐
                sb.append(String.format("|%-" + columnMaxLengths[i] + "s", columnStr[i]));
            }
            sb.append("|").append("\n");
        }
        sb.append(formatSeparator(columnMaxLengths));
        return sb.toString();
    }

    /**
     * 输出列名.
     *
     * @param resultSetMetaData 结果集的元数据对象.
     * @param columnMaxLengths  每一列最大长度的字符串的长度.
     * @throws SQLException
     */
    private static String formatColumnName(ResultSetMetaData resultSetMetaData, int[] columnMaxLengths)
            throws SQLException {
        StringBuilder sb = new StringBuilder();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s",
            // resultSetMetaData.getColumnName(i + 1));
            // 2020年11月4日 修改为左对齐
            sb.append(String.format("|%-" + columnMaxLengths[i] + "s", resultSetMetaData.getColumnName(i + 1)));
        }
        sb.append("|").append("\n");
        return sb.toString();
    }

    /**
     * 输出分隔符.
     *
     * @param columnMaxLengths 保存结果集中每一列的最长的字符串的长度.
     */
    private static String formatSeparator(int[] columnMaxLengths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnMaxLengths.length; i++) {
            sb.append("+");
            // for (int j = 0; j < columnMaxLengths[i] + 1; j++) {
            for (int j = 0; j < columnMaxLengths[i]; j++) {
                sb.append("-");
            }
        }
        sb.append("+").append("\n");
        return sb.toString();
    }
}
