package com.github.superzhc.financial.desktop.control.utils;

import com.github.superzhc.financial.desktop.utils.ConvertTableUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
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
        view.getItems().clear();
        view.setItems(ConvertTableUtils.convert(table));

        // 清除掉原有的列
        view.getColumns().clear();
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
}
