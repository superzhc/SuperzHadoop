package com.github.superzhc.javafx;

import com.github.superzhc.fund.data.index.EastMoneyIndex;
import com.github.superzhc.news.data.Jin10;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.util.*;

/**
 * @author superz
 * @create 2022/7/14 14:11
 **/
public class TablesawController implements Initializable {
    @FXML
    private TableView tableView;


    /**
     * 初始化
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Table table = Jin10.news();

        // 添加数据
        List<Map<String, Object>> rows = new ArrayList<>();

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

        // 初始化列
        for (String columnName : table.columnNames()) {
            TableColumn column = new TableColumn(columnName);

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    Map<String,Object> row = (Map<String,Object>) param.getValue();
                    Object value = row.get(columnName);

                    return new SimpleObjectProperty<>(value);
                }
            });

            tableView.getColumns().add(column);
        }
    }
}
