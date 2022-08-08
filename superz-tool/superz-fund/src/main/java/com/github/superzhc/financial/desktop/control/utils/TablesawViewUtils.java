package com.github.superzhc.financial.desktop.control.utils;

import com.github.superzhc.common.javafx.TableViewUtils;
import com.github.superzhc.tablesaw.utils.ConvertTableUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import tech.tablesaw.api.Table;

import java.util.Map;

/**
 * @author superz
 * @create 2022/7/18 16:03
 **/
@Deprecated
public class TablesawViewUtils {
    public static void bind(TableView view, Table table) {
        // 清除掉原有的数据
        // view.getItems().clear();
        view.setItems(FXCollections.observableList(ConvertTableUtils.toMap(table)));

        // 清除掉原有的列
        view.getColumns().clear();

        // 2022年7月19日 add 新增序号列
        TableColumn serialNumberColumn = TableViewUtils.numbers("序号");
        view.getColumns().add(serialNumberColumn);

        // 初始化列
        for (String columnName : table.columnNames()) {
            TableColumn column = new TableColumn(columnName);
            view.getColumns().add(column);

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    Map<String, Object> row = (Map<String, Object>) param.getValue();
                    return new SimpleObjectProperty(row.get(columnName));
                }
            });
        }
    }

    public static void append(TableView view, Table table) {
        view.getItems().addAll(0, ConvertTableUtils.toMap(table));
    }
}
