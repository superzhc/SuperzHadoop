package com.github.superzhc.financial.desktop.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/5 10:37
 **/
public class IndexEstimateController implements Initializable {
    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtName;

    @FXML
    private DatePicker dpValuationDate;

    @FXML
    private TextField txtValuation;

    @FXML
    private ChoiceBox<String> cbValuationType;

    @FXML
    private TextField txtValuationPercentage;

    @FXML
    private TextField txtValuationLowest;

    @FXML
    private TextField txtValuationHighest;

    @FXML
    private TextField txtUnderestimalThreshold;

    @FXML
    private TextField txtOverestimateThreshold;

    @FXML
    private TextField txtDividendYield;

    @FXML
    private TextField txtRoe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbValuationType.setItems(FXCollections.observableArrayList("PE", "PB"));
        cbValuationType.setValue("PE");

        Tooltip tip=new Tooltip("PE：市盈率，PB：市净率，适用于周期行业");
        cbValuationType.setTooltip(tip);
    }


}
