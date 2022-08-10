package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.FormUtils;
import com.github.superzhc.financial.data.fund.EastMoneyFund;
import com.github.superzhc.financial.desktop.control.utils.TablesawViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import tech.tablesaw.api.Table;

import java.util.Map;

/**
 * @author superz
 * @create 2022/8/9 11:13
 **/
public class FundController {
    @FXML
    private TextField txtFundCode;

    @FXML
    private HBox container;

    @FXML
    public void btnGetBasicInfo(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Map<String, String> fundInfo = EastMoneyFund.fundNew(fundCode);

        HBox code = FormUtils.text("代码", fundInfo.get("code"));
        HBox name = FormUtils.text("名称", fundInfo.get("name"));
        HBox type = FormUtils.text("类型", fundInfo.get("type"));
        HBox indexCode = FormUtils.text("指数代码", fundInfo.get("index_code"));
        HBox indexName = FormUtils.text("指数名称", fundInfo.get("index_name"));
        HBox riskLevel = FormUtils.choiceBox("风险等级", fundInfo.get("risk_level"), "1", "2", "3", "4", "5");

        GridPane gridPane = FormUtils.layout(590, 320);
        FormUtils.addCell(gridPane, code, 0, 0);
        FormUtils.addCell(gridPane, name, 1, 0);
        FormUtils.addCell(gridPane, type, 0, 1);
        FormUtils.addCell(gridPane, indexCode, 0, 2);
        FormUtils.addCell(gridPane, indexName, 1, 2);
        FormUtils.addCell(gridPane, riskLevel, 0, 3);

        show(gridPane);
    }

    @FXML
    public void btnGetRank(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundPeriodRank(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnGetScale(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundScale(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnUpdatePosition(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundUpdatePosition(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnGetFenHong(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundFenHong(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnGetStocks(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundStocks(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnPositionRatio(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundPositionRatio(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnIndustryComponent(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundIndustryComponent(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnNetWorthHistory(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }

        Table table = EastMoneyFund.fundNetHistory(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    @FXML
    public void btnRealNetWorth(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("请输入Fund Code");
            return;
        }


        Table table = EastMoneyFund.fundRealNet(fundCode);
        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        show(tv);
    }

    private void show(Region region) {
        region.prefWidthProperty().bind(container.prefWidthProperty().subtract(container.getPadding().getLeft() + container.getPadding().getRight()));

        container.getChildren().clear();
        container.getChildren().add(region);
    }
}
