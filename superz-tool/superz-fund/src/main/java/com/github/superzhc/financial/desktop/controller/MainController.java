package com.github.superzhc.financial.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/15 1:35
 */
public class MainController implements Initializable {
    @FXML
    private TabPane contentTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
//            Tab indexInfoTab = new Tab("指数信息");
//            indexInfoTab.setContent(FXMLLoader.load(getClass().getResource("../view/index_info.fxml")));
//            contentTab.getTabs().add(indexInfoTab);
//
//            Tab fundInfoTab = new Tab("基金信息");
//            fundInfoTab.setContent(FXMLLoader.load(getClass().getResource("../view/fund_info.fxml")));
//            contentTab.getTabs().add(fundInfoTab);

            Tab indexHistoryTab = new Tab("histroy"/*"指数历史数据"*/);
            indexHistoryTab.setContent(FXMLLoader.load(getClass().getResource("../view/index_history.fxml")));
            contentTab.getTabs().add(indexHistoryTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
