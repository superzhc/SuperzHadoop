package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.javafx.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/7/14 15:52
 **/
public class IndexInfoController {
    @FXML
    private TextField txtIndexCode;

    @FXML
    private TextField txtIndexName;
    @FXML
    private TextField txtIndexType;
    // 发布时间
    @FXML
    private TextField txtPublishDate;
    @FXML
    private TextArea txtIndexDescription;

    public void queryIndex(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("提示", "请输入指数 Code");
            return;
        }
        String csIndexCode = indexCode.split("\\.")[0];
        // 指数基本信息
        String basicUrl = String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-basic-info/%s", csIndexCode);
        String basicResult = HttpRequest.get(basicUrl).userAgent(UA).body();
        Map<String, ?> basicMap = JsonUtils.map(basicResult, "data");

        txtIndexName.setText(String.valueOf(basicMap.get("indexShortNameCn")));
        txtIndexType.setText(String.valueOf(basicMap.get("indexType")));
        txtPublishDate.setText(String.valueOf(basicMap.get("publishDate")));
        txtIndexDescription.setText(String.valueOf(basicMap.get("indexCnDesc")));
    }
}
