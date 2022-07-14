package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.fund.data.index.IndexData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import tech.tablesaw.api.Row;
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

        // 清除掉原有的数据
        tableView.getItems().clear();
        // 添加数据
        List<Map<String, Object>> rows = new ArrayList<>();

        // TODO：优化类型问题

        Iterator<Row> iterator = table.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> r = new HashMap<>();

            Row row = iterator.next();
            for (String columnName : table.columnNames()) {
                r.put(columnName, row.getObject(columnName));
            }
            rows.add(r);
        }
        ObservableList data = FXCollections.observableList(rows);
        tableView.setItems(data);

        // 清除掉原有的列
        tableView.getColumns().clear();
        // 初始化列
        for (String columnName : table.columnNames()) {
            TableColumn column = new TableColumn(columnName);

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    Map<String, Object> row = (Map<String, Object>) param.getValue();
                    Object value = row.get(columnName);

                    return new SimpleObjectProperty<>(value);
                }
            });

            tableView.getColumns().add(column);
        }
    }
}
