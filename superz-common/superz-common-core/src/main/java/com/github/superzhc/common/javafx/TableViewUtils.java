package com.github.superzhc.common.javafx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static <T> List<TableColumn<Map<String, T>, T>> bind(List<Map<String, T>> data) {
        List<TableColumn<Map<String, T>, T>> columns = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0, len = data.size(); i < len; i++) {
            Map<String, T> row = data.get(i);
            for (Map.Entry<String, T> item : row.entrySet()) {
                String columnName = item.getKey();
                if (!columnNames.contains(columnName)) {
                    TableColumn<Map<String, T>, T> column = new TableColumn<>(columnName);
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, T>, T>, ObservableValue<T>>() {
                        @Override
                        public ObservableValue<T> call(TableColumn.CellDataFeatures<Map<String, T>, T> param) {
                            Map<String, T> currentRow = param.getValue();
                            T value = currentRow.get(columnName);
                            return new SimpleObjectProperty<T>(value);
                        }
                    });
                    columns.add(column);
                    columnNames.add(columnName);
                }
            }
        }
        return columns;
    }
}
