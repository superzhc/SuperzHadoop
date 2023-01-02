package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.data.fund.EastMoneyFund;
import com.github.superzhc.financial.data.index.CSIndex;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/1 15:32
 **/
public class FundRecordAddController implements Initializable {
    @FXML
    private ComboBox<String> cbIndexCode;

    @FXML
    private TextField txtIndexName;

    @FXML
    private ComboBox<String> cbFundCode;

    @FXML
    private TextField txtFundName;

    @FXML
    private DatePicker dpBuyDate;

    @FXML
    private TextField txtInvest;

    @FXML
    private TextField txtNetWorth;

    @FXML
    private TextField txtShare;

    @FXML
    private TextField txtWorth;

    @FXML
    private TextField txtRate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final LocalDate now = LocalDate.now();
        dpBuyDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        DayOfWeek dayOfWeek = item.getDayOfWeek();
                        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || item.isAfter(now)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void btnSearchFundByName(ActionEvent actionEvent) {
        String fundName = txtFundName.getText();
        if (null == fundName || fundName.trim().length() == 0) {
            DialogUtils.error("请输入基金名称");
            return;
        }

        Table table = EastMoneyFund.funds();
        table = table.where(table.stringColumn("name").containsString(fundName));
        List<String> fundCodes = table.stringColumn("code").asList();
        cbFundCode.setItems(FXCollections.observableList(fundCodes));
        txtFundName.setText(null);
        DialogUtils.info("查询成功");
    }

    @FXML
    public void btnSearchIndexByFundCode(ActionEvent actionEvent) {
        String fundCode = cbFundCode.getValue();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("消息", "Fund Code 不能为空");
            return;
        }
        getFundBasicInfo(fundCode);
    }

    @FXML
    public void fundCodeChange(ActionEvent actionEvent) {
        String fundCode = cbFundCode.getValue();
        getFundBasicInfo(fundCode);
    }

    private void getFundBasicInfo(String fundCode) {
        Map<String, Object> fundInfo = com.github.superzhc.data.fund.EastMoneyFund.fund(fundCode);
        txtFundName.setText(String.valueOf(fundInfo.get("name")));
        cbIndexCode.setValue(String.valueOf(fundInfo.get("index_code")));
        txtIndexName.setText(String.valueOf(fundInfo.get("index_name")));
        txtRate.setText(String.valueOf(fundInfo.get("real_rate")));
    }

    @FXML
    public void btnSearchIndexByName(ActionEvent actionEvent) {
        String indexName = txtIndexName.getText();
        if (null == indexName || indexName.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数的名称");
            return;
        }

        Table table = CSIndex.indices(indexName);

        if (null == table || table.rowCount() == 0) {
            DialogUtils.warning("未查询到数据");
            return;
        }

        List<String> indexCodes = table.stringColumn("index_code").asList();
        cbIndexCode.getItems().setAll(indexCodes);
        txtIndexName.setText(null);
        DialogUtils.info("消息", "查询成功");
    }

    @FXML
    public void indexCodeChange(ActionEvent actionEvent) {

    }
}
