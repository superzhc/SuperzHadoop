package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.data.index.EastMoneyIndex;
import com.github.superzhc.financial.desktop.control.utils.TableViewUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/19 0:47
 */
public class IndicesEMController implements Initializable {
    @FXML
    private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Table table = EastMoneyIndex.indices();
        TableViewUtils.bind(tableView, table);
    }
}
