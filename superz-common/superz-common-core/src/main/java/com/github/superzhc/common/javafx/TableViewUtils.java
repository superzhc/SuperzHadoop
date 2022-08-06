package com.github.superzhc.common.javafx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.*;

/**
 * @author superz
 * @create 2022/7/26 16:26
 **/
public class TableViewUtils {
    public static <T, S> TableColumn<T, S> numbers() {
        return numbers("序号");
    }

    public static <T, S> TableColumn<T, S> numbers(String title) {
        TableColumn<T, S> idColumn = new TableColumn<>(title);
        idColumn.setCellFactory(new Callback<TableColumn<T, S>, TableCell<T, S>>() {
            @Override
            public TableCell<T, S> call(TableColumn<T, S> param) {
                TableCell<T, S> cell = new TableCell<T, S>() {
                    @Override
                    protected void updateItem(S item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);

                        if (!empty) {
                            int rowIndex = this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;

            }
        });
        return idColumn;
    }

    public static <T> TableView<Map<String, T>> clearAndBind(TableView<Map<String, T>> tv, List<Map<String, T>> data) {
        // 清除数据
        tv.setItems(null);

        // 清除列
        tv.getColumns().clear();

        return bind(tv, data);
    }

    public static <T> TableView<Map<String, T>> bind(TableView<Map<String, T>> tv, List<Map<String, T>> data) {
        // 绑定列
        List<TableColumn<Map<String, T>, T>> columns = createColumns(data);
        tv.getColumns().addAll(columns);

        // 绑定数据
        tv.setItems(FXCollections.observableList(data));
        return tv;
    }

    public static <T> List<TableColumn<Map<String, T>, T>> createColumns(List<Map<String, T>> data) {
        List<TableColumn<Map<String, T>, T>> columns = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0, len = data.size(); i < len; i++) {
            Map<String, T> row = data.get(i);
            for (Map.Entry<String, T> item : row.entrySet()) {
                String columnName = item.getKey();
                if (!columnNames.contains(columnName)) {
                    TableColumn<Map<String, T>, T> column = createColumn(columnName);
                    columns.add(column);
                    columnNames.add(columnName);
                }
            }
        }
        return columns;
    }

    public static <T> List<TableColumn<Map<String, T>, T>> createColumns(String... columnNames) {
        Set<String> names = new LinkedHashSet<>(Arrays.asList(columnNames));
        List<TableColumn<Map<String, T>, T>> columns = new ArrayList<>(names.size());
        for (String name : names) {
            columns.add(createColumn(name));
        }
        return columns;
    }

    public static <T> TableColumn<Map<String, T>, T> createColumn(String columnName) {
        TableColumn<Map<String, T>, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, T>, T>, ObservableValue<T>>() {
            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<Map<String, T>, T> param) {
                Map<String, T> currentRow = param.getValue();
                T value = currentRow.get(columnName);
                return new SimpleObjectProperty<T>(value);
            }
        });

        // 2022年8月4日 将宽度控制在一个范围
        column.setMaxWidth(180);
        return column;
    }
}
