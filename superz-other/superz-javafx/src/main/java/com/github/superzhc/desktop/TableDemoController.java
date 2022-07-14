package com.github.superzhc.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/14 1:22
 */
public class TableDemoController implements Initializable {

    @FXML
    private TableView<TableData> table;
    @FXML
    private TableColumn<TableData, String> c1;
    @FXML
    public TableColumn<TableData, String> c2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        c1.setCellValueFactory(cellData -> cellData.getValue().field1Property());
        c2.setCellValueFactory(cellData -> cellData.getValue().field2Property());

        ObservableList<TableData> data= FXCollections.observableArrayList();
        data.add(new TableData("f1.1","f2.1"));
        data.add(new TableData("f1.2","f2.2"));
        table.setItems(data);
    }
}
