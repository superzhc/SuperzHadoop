package com.github.superzhc.tablesaw.utils;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2022/7/15 15:27
 **/
public class ConvertTableUtils {
    public static List<Map<String, Object>> toMap(Table table) {
        if (null == table || table.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        Iterator<Row> iterator = table.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = new LinkedHashMap<>();

            Row row = iterator.next();
            for (String columnName : table.columnNames()) {
                ColumnType columnType = row.getColumnType(columnName);
                // 2022年8月11日 fix bug：【数值类型在null的情况下会返回NAN显示有点问题】
                // 先判定数据是否为null，如果未null不进行类型操作
                Object value = row.getObject(columnName);
                if (null == value) {
                    map.put(columnName, null);
                    continue;
                }

                if (ColumnType.SHORT.equals(columnType)) {
                    value = row.getShort(columnName);
                } else if (ColumnType.INTEGER.equals(columnType)) {
                    value = row.getInt(columnName);
                } else if (ColumnType.LONG.equals(columnType)) {
                    value = row.getLong(columnName);
                } else if (ColumnType.FLOAT.equals(columnType)) {
                    value = row.getFloat(columnName);
                } else if (ColumnType.BOOLEAN.equals(columnType)) {
                    value = row.getBoolean(columnName);
                } else if (ColumnType.STRING.equals(columnType)) {
                    value = row.getString(columnName);
                } else if (ColumnType.DOUBLE.equals(columnType)) {
                    // fix bug:double过长会使用科学计数法
                    // value = new SimpleDoubleProperty(row.getDouble(columnName));
                    //BigDecimal data = new BigDecimal(row.getDouble(columnName));
                    Double data = row.getDouble(columnName);
                    value = new DecimalFormat("0.00").format(data);
                } else if (ColumnType.LOCAL_DATE.equals(columnType)) {
                    LocalDate date = row.getDate(columnName);
                    value = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                } else if (ColumnType.LOCAL_TIME.equals(columnType)) {
                    LocalTime time = row.getTime(columnName);
                    value = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
                } else if (ColumnType.LOCAL_DATE_TIME.equals(columnType)) {
                    LocalDateTime dateTime = row.getDateTime(columnName);
                    value = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } else if (ColumnType.INSTANT.equals(columnType)) {
                    Instant instant = row.getInstant(columnName);
                    value = instant.toEpochMilli();
                } else if (ColumnType.TEXT.equals(columnType)) {
                    value = row.getText(columnName);
                }
                map.put(columnName, value);
            }

            maps.add(map);
        }

        return maps;
    }
}
