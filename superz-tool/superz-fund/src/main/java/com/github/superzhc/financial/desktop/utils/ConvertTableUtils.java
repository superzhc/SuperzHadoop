package com.github.superzhc.financial.desktop.utils;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.math.BigDecimal;
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
    public static ObservableList<ObservableMap<String, ObservableValue>> convert(Table table) {
        List<ObservableMap<String, ObservableValue>> list = new ArrayList<>();

        Iterator<Row> iterator = table.iterator();
        while (iterator.hasNext()) {
            Map<String, ObservableValue> map = new LinkedHashMap<>();

            Row row = iterator.next();
            for (String columnName : table.columnNames()) {
                ColumnType columnType = row.getColumnType(columnName);
                ObservableValue value;
                if (ColumnType.SHORT.equals(columnType)) {
                    value = new SimpleIntegerProperty(row.getShort(columnName));
                } else if (ColumnType.INTEGER.equals(columnType)) {
                    value = new SimpleIntegerProperty(row.getInt(columnName));
                } else if (ColumnType.LONG.equals(columnType)) {
                    value = new SimpleLongProperty(row.getLong(columnName));
                } else if (ColumnType.FLOAT.equals(columnType)) {
                    value = new SimpleFloatProperty(row.getFloat(columnName));
                } else if (ColumnType.BOOLEAN.equals(columnType)) {
                    value = new SimpleBooleanProperty(row.getBoolean(columnName));
                } else if (ColumnType.STRING.equals(columnType)) {
                    value = new SimpleStringProperty(row.getString(columnName));
                } else if (ColumnType.DOUBLE.equals(columnType)) {
                    // fix bug:double过长会使用科学计数法
                    // value = new SimpleDoubleProperty(row.getDouble(columnName));
                    //BigDecimal data = new BigDecimal(row.getDouble(columnName));
                    Double data = row.getDouble(columnName);
                    value = new SimpleStringProperty(new DecimalFormat("0.00").format(data));
                } else if (ColumnType.LOCAL_DATE.equals(columnType)) {
                    LocalDate date = row.getDate(columnName);
                    value = new SimpleStringProperty(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
                } else if (ColumnType.LOCAL_TIME.equals(columnType)) {
                    LocalTime time = row.getTime(columnName);
                    value = new SimpleStringProperty(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
                } else if (ColumnType.LOCAL_DATE_TIME.equals(columnType)) {
                    LocalDateTime dateTime = row.getDateTime(columnName);
                    value = new SimpleStringProperty(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else if (ColumnType.INSTANT.equals(columnType)) {
                    Instant instant = row.getInstant(columnName);
                    value = new SimpleLongProperty(instant.toEpochMilli());
                } else if (ColumnType.TEXT.equals(columnType)) {
                    value = new SimpleStringProperty(row.getText(columnName));
                } else {
                    value = new SimpleObjectProperty(row.getObject(columnName));
                }
                map.put(columnName, value);
            }

            ObservableMap<String, ObservableValue> observableMap = FXCollections.observableMap(map);
            list.add(observableMap);
        }

        ObservableList<ObservableMap<String, ObservableValue>> data = FXCollections.observableList(list);
        return data;
    }
}
