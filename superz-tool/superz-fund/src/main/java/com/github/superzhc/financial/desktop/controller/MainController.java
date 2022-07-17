package com.github.superzhc.financial.desktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/15 1:35
 */
public class MainController implements Initializable {
    @FXML
    private MenuBar menu;

    @FXML
    private TabPane contentTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            Menu index = new Menu("Index");
            MenuItem indexBasic = new MenuItem("Basic");
            // 设置快捷键
            //indexBasic.setAccelerator(KeyCombination.valueOf("ctrl+i+b"));
            // 设置点击事件
            indexBasic.setOnAction(event -> {
                try {
                    Tab indexInfoTab = new Tab("Index Basic");
                    indexInfoTab.setContent(FXMLLoader.load(getClass().getResource("../view/index_info.fxml")));
                    contentTab.getTabs().add(indexInfoTab);
                    contentTab.getSelectionModel().select(indexInfoTab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            //创建分割线
            SeparatorMenuItem separator1 = new SeparatorMenuItem();

            MenuItem indexHistroy = new MenuItem("History");
            indexHistroy.setOnAction(event -> {
                try {
                    Tab indexHistoryTab = new Tab("Index History"/*"指数历史数据"*/);
                    indexHistoryTab.setContent(FXMLLoader.load(getClass().getResource("../view/index_history.fxml")));
                    contentTab.getTabs().add(indexHistoryTab);
                    contentTab.getSelectionModel().select(indexHistoryTab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            index.getItems().addAll(indexBasic, separator1, indexHistroy);

            Menu fund = new Menu("fund");

            MenuItem fundBasic = new MenuItem("Fund Basic");
            fundBasic.setOnAction(event -> {
                try {
                    Tab fundInfoTab = new Tab("基金信息");
                    fundInfoTab.setContent(FXMLLoader.load(getClass().getResource("../view/fund_info.fxml")));
                    contentTab.getTabs().add(fundInfoTab);
                    contentTab.getSelectionModel().select(fundInfoTab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            fund.getItems().addAll(fundBasic);

            menu.getMenus().addAll(index, fund);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
