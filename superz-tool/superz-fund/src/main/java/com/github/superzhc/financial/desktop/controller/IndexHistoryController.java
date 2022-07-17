package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.desktop.utils.ConvertTableUtils;
import com.github.superzhc.fund.data.index.IndexData;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/7/15 1:18
 */
public class IndexHistoryController {
    @FXML
    private TextField txtIndexCode;

    @FXML
    private TableView tableView;

    public void btnSearch(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数Code");
            return;
        }

        Table table = IndexData.history(indexCode);
        table=table.sortDescendingOn("date");
        // System.out.println(table.structure().printAll());

        // 清除掉原有的数据
        tableView.getItems().clear();
        tableView.setItems(ConvertTableUtils.convert(table));

        // 清除掉原有的列
        tableView.getColumns().clear();
        // 初始化列
        for (String columnName : table.columnNames()) {
            TableColumn column = new TableColumn(columnName);
            tableView.getColumns().add(column);

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
