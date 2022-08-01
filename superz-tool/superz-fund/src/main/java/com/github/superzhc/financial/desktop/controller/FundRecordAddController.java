package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.data.fund.EastMoneyFund;
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
import java.time.LocalDate;
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

                        if (item.isAfter(now)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void btnSearchFundCode(ActionEvent actionEvent) {
//        String indexCode=
    }

    @FXML
    public void btnSearchIndexCode(ActionEvent actionEvent) {
        String indexName = txtIndexName.getText();
        if (null == indexName || indexName.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数名称");
            return;
        }


    }

    @FXML
    public void btnSearchIndex(ActionEvent actionEvent) {
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
        Map<String, String> fundInfo = EastMoneyFund.fundNew(fundCode);
        txtFundName.setText(fundInfo.get("name"));
        cbIndexCode.setValue(fundInfo.get("index_code"));
        txtIndexName.setText(fundInfo.get("index_name"));
        txtRate.setText(fundInfo.get("rate"));
    }
}
