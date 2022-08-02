package com.github.superzhc.common.javafx.controller;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.javafx.DialogUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/28 10:01
 **/
public class ToolHtmlController implements Initializable {
    /*相对于当前文件的地址*/
    public static final String FXML_PATH = "../tool_html.fxml";

    @FXML
    private TextField txtUrl;

    private ChoiceBox<String> cbRequestMethod;

    private ComboBox<String> cbResponseCharset;

    @FXML
    private TextArea txtHtml;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbRequestMethod.setItems(FXCollections.observableArrayList("GET", "POST"));
        cbRequestMethod.setValue("GET");

        cbResponseCharset.setItems(FXCollections.observableArrayList("UTF-8", "GB2312", "GBK"));
        cbResponseCharset.setValue("UTF-8");
    }

    @FXML
    public void btnGetHtml(ActionEvent actionEvent) {
        String url = txtUrl.getText();
        if (null == url || url.trim().length() == 0) {
            DialogUtils.error("消息", "请输入请求地址");
            return;
        }

        String result = HttpRequest.get(url).body(cbResponseCharset.getValue());
        txtHtml.setText(result);
    }
}
