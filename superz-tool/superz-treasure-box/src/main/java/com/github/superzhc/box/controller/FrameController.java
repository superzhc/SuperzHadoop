package com.github.superzhc.box.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class FrameController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(FrameController.class);

    private Scene scene;

    @FXML
    private BorderPane frame;

    @FXML
    private HBox customMenuBar;

    @FXML
    private MenuBar menu;

    @FXML
    private TabPane main;

    private Tab homeTab;

    @FXML
    private HBox statusBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initHomePage();
    }

    private void initHomePage() {
        homeTab = new Tab("首页");
        homeTab.setClosable(false);
        try {
            FXMLLoader homeLoader = new FXMLLoader(this.getClass().getResource("/views/home.fxml"));
            Parent home = homeLoader.load();
            homeTab.setContent(home);

            main.getTabs().add(homeTab);
            main.getSelectionModel().select(homeTab);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
