package com.github.superzhc.fund.tablesaw.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.TableBuildingUtils;
import tech.tablesaw.io.html.HtmlWriteOptions;
import tech.tablesaw.io.html.HtmlWriter;

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

    public static void write2Html(Table table) {
        try {
            // 原生太丑了
            String fileName = String.format("table%s_%s.html", (null == table.name() || table.name().trim().length() == 0) ? "" : ("_" + table.name()), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            HtmlWriteOptions options = HtmlWriteOptions.builder(PlotUtils.file(fileName)).build();
            table.write().usingOptions(options);
        } catch (Exception e) {
            log.error("Write Html error .", e);
        }
    }
}
