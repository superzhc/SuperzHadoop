package com.github.superzhc.db.schema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author superz
 * @create 2021/8/4 11:33
 */
public abstract class Schema {

    public String markdown(String title) {
        StringBuilder sb = new StringBuilder();
        // 标题
        sb.append("# `").append(title).append("`").append(lines(2));
        // 获取所有表
        List<String> tables = tables();
        for (String table : tables) {
            // 表名称
            sb.append("## `").append(table).append("`").append(lines(2));
            // 获取表的描述
            String description = describe(table);
            if (null != description && description.trim().length() > 0) {
                sb.append("> ").append(description).append(lines(2));
            }
            // 表格字段
            LinkedHashMap<String, String> headers = columnHeaders();
            Set<Map.Entry<String, String>> set = headers.entrySet();
            StringBuilder header1 = new StringBuilder("|");
            StringBuilder header2 = new StringBuilder("|");
            for (Map.Entry<String, String> entry : set) {
                header1.append(entry.getValue()).append("|");
                header2.append("-----|");
            }
            sb.append(header1).append(line());
            sb.append(header2).append(line());

            List<Map<String, Object>> columns = columns(table);
            for (Map<String, Object> column : columns) {
                StringBuilder columnSb = new StringBuilder("|");
                for (Map.Entry<String, String> entry : set) {
                    Object value = column.get(entry.getKey());
                    value = null == value ? value : value.toString().replace("\r\n", "");
                    columnSb.append(value).append("|");
                }
                sb.append(columnSb).append(line());
            }
            sb.append(line());
        }

        return sb.toString();
    }

    private String line() {
        return lines(1);
    }

    private String lines(int num) {
        StringBuilder sb = new StringBuilder();
        while (num-- > 0) {
            sb.append("\r\n");
        }
        return sb.toString();
    }

    protected abstract List<String> tables();

    protected abstract String describe(String table);

    protected abstract LinkedHashMap<String, String> columnHeaders();

    protected abstract List<Map<String, Object>> columns(String table);
}
