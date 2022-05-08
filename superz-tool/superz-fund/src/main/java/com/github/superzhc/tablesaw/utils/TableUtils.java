package com.github.superzhc.tablesaw.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.TableBuildingUtils;
import tech.tablesaw.plotly.Plot;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/4/7 9:07
 **/
public class TableUtils {
    private static final Logger log = LoggerFactory.getLogger(TableUtils.class);

    public static class FundColumnType implements Function<String, Optional<ColumnType>> {

        @Override
        public Optional<ColumnType> apply(String s) {
            ColumnType ct = null;
            if (null != s) {
                switch (s.toLowerCase()) {
                    case "code":
                    case "fund_code":
                    case "fundcode":
                    case "fund.code":
                    case "indexcode":
                    case "gu_code":
                    case "fcode":
                    case "代码":
                    case "指数代码":
                    case "gpdm":
                    case "zqdm":
                    case "etfcode":
                        ct = ColumnType.STRING;
                        break;
                }
            }
            return Optional.ofNullable(ct);
        }
    }

    public static Table build(List<String> columnNames, List<String[]> dataRows) {
        return TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByFunction(new FundColumnType()));
    }

    public static Table json2Table(JsonNode json) {
        return json2Table(null, json);
    }

    public static Table json2Table(String tableName, JsonNode json) {
        return map2Table(tableName, JsonUtils.map(json));
    }

    public static Table map2Table(Map<String, ?> map) {
        return map2Table(null, map);
    }

    public static Table map2Table(String tableName, Map<String, ?> map) {
//        StringColumn keyColumn = StringColumn.create("KEY");
//        StringColumn valueColumn = StringColumn.create("VALUE");
//
//        for (Map.Entry<String, ?> entry : map.entrySet()) {
//            keyColumn.append(entry.getKey());
//            valueColumn.append(null == entry.getValue() ? null : entry.getValue().toString());
//        }
//
//        Table table = Table.create(tableName, keyColumn, valueColumn);

//        Table table = Table.create(tableName);
//        for (Map.Entry<String, ?> entry : map.entrySet()) {
//            table.addColumns(StringColumn.create(entry.getKey(), null == entry.getValue() ? null : entry.getValue().toString()));
//        }

        List<String> columnNames = new ArrayList<>(map.keySet());
        String[] row = new String[columnNames.size()];
        for (int i = 0, len = columnNames.size(); i < len; i++) {
            String columnName = columnNames.get(i);
            Object value = map.get(columnName);
            row[i] = null == value ? null : value.toString();
        }

        List<String[]> dataRows = new ArrayList<>();
        dataRows.add(row);

        Table table = build(columnNames, dataRows);

        return table;
    }

    public static Table timestamp2Date(Table table, String columnName) {
        DateColumn dc = table.longColumn(columnName).asDateTimes(ZoneOffset.ofHours(+8)).date().setName(columnName);
        return table.replaceColumn(columnName, dc);
    }

    public static Table addConstantColumn(Table table, String columnName, int value) {
        IntColumn intColumn = IntColumn.create(columnName);
        return addConstantColumn(table, intColumn, value);
    }

    public static Table addConstantColumn(Table table, String columnName, long value) {
        LongColumn longColumn = LongColumn.create(columnName);
        return addConstantColumn(table, longColumn, value);
    }

    public static Table addConstantColumn(Table table, String columnName, double value) {
        DoubleColumn doubleColumn = DoubleColumn.create(columnName);
        return addConstantColumn(table, doubleColumn, value);
    }

    public static Table addConstantColumn(Table table, String columnName, String value) {
        StringColumn stringColumn = StringColumn.create(columnName);
        return addConstantColumn(table, stringColumn, value);
    }

    public static <T> Table addConstantColumn(Table table, Column<T> column, T value) {
        int count = table.rowCount();
        for (int i = 0; i < count; i++) {
            column.append(value);
        }
        table.addColumns(column);
        return table;
    }

    public static Table rename(Table table, String columnName, String newColumnName) {
        table.column(columnName).setName(newColumnName);
        return table;
    }

    public static Table rename(Table table, Map<String, String> nameMap) {
        List<String> columnNames = table.columnNames();
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            if (columnNames.contains(entry.getKey())) {
                table.column(entry.getKey()).setName(entry.getValue());
            }
        }
        return table;
    }

    public static Table rename(Table table, List<String> newNames) {
        for (int i = 0, len = newNames.size(); i < len; i++) {
            String newName = newNames.get(i);

            // 支持占位符 _、-不做重命名
            if ("_".equals(newName) || "-".equals(newName)) {
                continue;
            }

            table.column(i).setName(newName);
        }
        return table;
    }

    public static void write2Html(Table table) {
        try {
            // // 原生太丑了
            String fileName = String.format("table%s_%s.html", (null == table.name() || table.name().trim().length() == 0) ? "" : ("_" + table.name()), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            // HtmlWriteOptions options = HtmlWriteOptions.builder(PlotUtils.file(fileName)).build();
            // table.write().usingOptions(options);

            StringBuilder htmlSb = new StringBuilder();
            // Head
            htmlSb.append("<!DOCTYPE html>\n");
            htmlSb.append("<html>\n");
            htmlSb.append("<head>\n");
            htmlSb.append(" <title>" + ((null == table.name() || table.name().trim().length() == 0) ? "Tablesaw" : table.name()) + "</title>\n");
            htmlSb.append(" <link rel=\"stylesheet\" href=\"https://layui.itze.cn/layui-v2.6.8/layui/css/layui.css\">\n");
            htmlSb.append("</head>\n");

            List<String> columnNames = table.columnNames();

            boolean isMulti = table.rowCount() > 1;
            if (isMulti) {
                ArrayNode cols = JsonUtils.mapper().createArrayNode();
                for (String columnName : columnNames) {
                    ObjectNode col = JsonUtils.mapper().createObjectNode();
                    col.put("field", columnName);
                    col.put("title", columnName);
                    col.put("width", 180);
                    cols.add(col);
                }

                ArrayNode data = JsonUtils.mapper().createArrayNode();
                for (int i = 0, size = table.rowCount(); i < size; i++) {
                    ObjectNode item = JsonUtils.mapper().createObjectNode();

                    Row row = table.row(i);
                    for (String columnName : columnNames) {
                        Object value = row.getObject(columnName);
                        item.put(columnName, null == value ? null : value.toString());
                    }
                    data.add(item);
                }


                htmlSb.append("<body>\n");
                htmlSb.append("<table id=\"tablesaw\" lay-filter=\"tablesaw\"></table>\n");
                htmlSb.append("<script src=\"https://layui.itze.cn/layui-v2.6.8/layui/layui.js\"></script>\n");
                htmlSb.append("<script>\n");
                htmlSb.append("layui.use('table', function(){\n");
                htmlSb.append("var table = layui.table;\n");
                htmlSb.append("table.render({\n");
                htmlSb.append("elem: '#tablesaw'\n");
                htmlSb.append(",height: 312\n");
                htmlSb.append(",cols: [" + JsonUtils.string(cols) + "]\n");
                htmlSb.append(",data:" + JsonUtils.string(data));
                htmlSb.append("\n});\n");
                htmlSb.append("});\n");
                htmlSb.append("</script>\n");
                htmlSb.append("</body>\n");
            } else {
                StringBuilder sb = new StringBuilder();
                String temp = "<div class=\"layui-form-item\">\n  <label class=\"layui-form-label\">%s</label>\n  <div class=\"layui-input-block\">%s</div>\n</div>\n";
                for (int i = 0, size = table.rowCount(); i < size; i++) {
                    Row row = table.row(i);
                    for (String columnName : columnNames) {
                        Object value = row.getObject(columnName);

                        if (null == value || value.toString().trim().length() == 0) {
                            continue;
                        }

                        String str;
                        if (value.toString().length() < 128) {
                            str = String.format("<input type=\"text\" name=\"%s\" value=\"%s\" disabled class=\"layui-input\">", columnName, value);
                        } else {
                            str = String.format("<textarea name=\"%s\" disabled class=\"layui-textarea\">%s</textarea>", columnName, value);
                        }
                        sb.append(String.format(temp, columnName, str));
                    }
                }

                htmlSb.append("<body>\n");
                htmlSb.append("<form class=\"layui-form layui-form-pane\">");
                htmlSb.append(sb);
                htmlSb.append("</form>");
                htmlSb.append("<script src=\"https://layui.itze.cn/layui-v2.6.8/layui/layui.js\"></script>\n");
                htmlSb.append("<script>\n");
                htmlSb.append("layui.use('form', function(){\n");
                htmlSb.append("var form = layui.form;\n");
                htmlSb.append("});\n");
                htmlSb.append("</script>\n");
                htmlSb.append("</body>\n");
            }

            htmlSb.append("</html>");
            Plot.show(htmlSb.toString(), PlotUtils.file(fileName));
        } catch (Exception e) {
            log.error("Write Html error .", e);
        }
    }
}
