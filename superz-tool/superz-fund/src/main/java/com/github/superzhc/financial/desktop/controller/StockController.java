package com.github.superzhc.financial.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * @author superz
 * @create 2022/8/9 16:27
 **/
public class StockController {
    @FXML
    private TextField txtStockCode;

    @FXML
    private HBox container;

    private void show(Region region) {
        region.prefWidthProperty().bind(container.prefWidthProperty().subtract(container.getPadding().getLeft() + container.getPadding().getRight()));

        container.getChildren().clear();
        container.getChildren().add(region);
    }
}
