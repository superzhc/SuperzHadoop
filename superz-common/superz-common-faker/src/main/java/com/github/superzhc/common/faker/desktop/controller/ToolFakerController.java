package com.github.superzhc.common.faker.desktop.controller;

import com.github.javafaker.Faker;
import com.github.superzhc.common.javafx.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/5 17:39
 **/
public class ToolFakerController implements Initializable {
    public static final String FXML_PATH = "../view/tool_faker.fxml";

    public static URL getFxmlPath() {
        return ToolFakerController.class.getResource(FXML_PATH);
    }

    private Faker faker;

    @FXML
    private TextField txtExpression;

    @FXML
    private TextArea txtResult;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        faker = Faker.instance(Locale.CHINA);
    }

    @FXML
    public void btnGenerateDataByExpression(ActionEvent actionEvent) {
        String expression = txtExpression.getText();
        if (null == expression || expression.trim().length() == 0) {
            DialogUtils.error("请输入表达式");
            return;
        }

        try {
            String result = faker.expression(expression);
            txtResult.setText(result);
        } catch (Exception e) {
            DialogUtils.error("生成表达式异常:" + e.getMessage());
            return;
        }
    }
}
