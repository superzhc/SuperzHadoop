package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.data.index.EastMoneyIndex;
import com.github.superzhc.financial.desktop.control.utils.TableViewUtils;
import com.github.superzhc.financial.data.index.IndexData;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/7/19 0:15
 */
public class IndexDataController {
    @FXML
    private TextField txtIndexCode;

    @FXML
    private TabPane indexDataTabPane;

    public void btnSearchHistory(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数Code");
            return;
        }

        String title = String.format("History[%s]", indexCode);

        Table table = IndexData.history(indexCode);
        table = table.sortDescendingOn("date");
        createOrOpenTab(title, table);
    }

    public void btnTranceIndex(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数Code");
            return;
        }

        String title = String.format("TranceIndex[%s]", indexCode);

        Table table = EastMoneyIndex.tranceIndex(indexCode);
        createOrOpenTab(title, table);
    }

    private void createOrOpenTab(String title, Table data) {
        ObservableList<Tab> openedTabs = indexDataTabPane.getTabs();
        for (Tab openedTab : openedTabs) {
            if (title.equals(openedTab.getText())) {
                indexDataTabPane.getSelectionModel().select(openedTab);
                return;
            }
        }

        Tab tab = new Tab(title);
        TableView view = new TableView();
        TableViewUtils.bind(view, data);
        tab.setContent(view);
        indexDataTabPane.getTabs().add(tab);
        indexDataTabPane.getSelectionModel().select(tab);
    }
}
