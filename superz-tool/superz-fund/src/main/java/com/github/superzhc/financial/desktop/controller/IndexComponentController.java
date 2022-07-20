package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.data.index.CSIndex;
import com.github.superzhc.financial.data.index.SinaIndex;
import com.github.superzhc.financial.desktop.control.utils.TableViewUtils;
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
 * @create 2022/7/19 14:13
 **/
public class IndexComponentController implements Initializable {
    @FXML
    private TextField txtIndexCode;

    @FXML
    private ChoiceBox<String> cbDataSource;

    @FXML
    private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbDataSource.getItems().addAll("Sina", "CS");
    }

    public void btnSearch(ActionEvent actionEvent) {
        String ds = cbDataSource.getValue();
        if (null == ds || ds.trim().length() == 0) {
            DialogUtils.error("消息", "请选择数据源");
            return;
        }

        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数代码");
            return;
        }

        Table table = null;
        switch (ds) {
            case "Sina":
                table = SinaIndex.stocks(indexCode);
                break;
            case "CS":
                table = CSIndex.stocksWeight(indexCode);
                break;
        }
        if (null != table) {
            TableViewUtils.bind(tableView, table);
        }
    }
}
