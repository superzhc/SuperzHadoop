package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.data.index.*;
import com.github.superzhc.financial.desktop.control.utils.TablesawViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/19 0:47
 */
public class IndicesController implements Initializable {

    @FXML
    private ChoiceBox<String> cbDataSource;

    @FXML
    private TextField txtIndexName;

    @FXML
    private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbDataSource.getItems().addAll("EastMoney", "Sina", "CS", "JoinQuant");
    }

    public void btnSearchIndices(ActionEvent actionEvent) {
        Table table = getData(cbDataSource.getValue(), txtIndexName.getText());
        if (null != table) {
            TablesawViewUtils.bind(tableView, table);
        }
    }

    private Table getData(String ds, String indexName) {
        Table table = null;
        switch (ds) {
            case "EastMoney":
            case "EM":
                table = EastMoneyIndex.indices();
                if (null != indexName && indexName.trim().length() > 0) {
                    table = table.where(table.stringColumn("index_name").containsString(indexName));
                }
                break;
            case "CNIndex":
            case "CN":
            case "国证":
                table = CNIndex.indices();
                break;
            case "CSIndex":
            case "CS":
            case "中证":
                if (null != indexName && indexName.trim().length() > 0) {
                    table = CSIndex.indices(indexName);
                } else {
                    table = CSIndex.indices();
                }
                break;
            case "Sina":
                table = SinaIndex.indices();
                if (null != indexName && indexName.trim().length() > 0) {
                    table = table.where(table.stringColumn("name").containsString(indexName));
                }
                break;
            case "JoinQuant":
                table = JoinquantIndex.indices();
                break;
        }
        return table;
    }
}
