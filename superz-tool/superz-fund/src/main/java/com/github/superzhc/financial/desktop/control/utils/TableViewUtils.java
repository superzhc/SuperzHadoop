package com.github.superzhc.financial.desktop.control.utils;

import com.github.superzhc.financial.desktop.utils.ConvertTableUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/7/18 16:03
 **/
public class TableViewUtils {
    public static void bind(TableView view, Table table) {
        // 清除掉原有的数据
        // view.getItems().clear();
        view.setItems(ConvertTableUtils.convert(table));

        // 清除掉原有的列
        view.getColumns().clear();

        // 2022年7月19日 add 新增序号列
        TableColumn serialNumberColumn = new TableColumn("序号");
        serialNumberColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
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
        view.getColumns().add(serialNumberColumn);

        // 初始化列
        for (String columnName : table.columnNames()) {
            TableColumn column = new TableColumn(columnName);
            view.getColumns().add(column);

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    ObservableMap<String, ObservableValue> row = (ObservableMap<String, ObservableValue>) param.getValue();
                    return row.get(columnName);
                }
            });
        }
    }

    public static void append(TableView view, Table table) {
        view.getItems().addAll(0, ConvertTableUtils.convert(table));
    }
}
