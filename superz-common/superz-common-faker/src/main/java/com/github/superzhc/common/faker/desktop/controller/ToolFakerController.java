package com.github.superzhc.common.faker.desktop.controller;

import com.github.javafaker.Faker;
import com.github.superzhc.common.faker.utils.ExpressionUtils;
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
    public static final String FXML_PATH = "../../view/tool_faker.fxml";

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

    // region================================模板=================================================
    @FXML
    public void btnTemplateName(ActionEvent actionEvent) {
        txtExpression.setText(ExpressionUtils.NAME);
    }

//    @FXML
//    public void btnTemplateName2(ActionEvent actionEvent) {
//        txtExpression.setText(ExpressionUtils.name());
//    }

    @FXML
    public void btnTemplateAge(ActionEvent actionEvent) {
        txtExpression.setText(ExpressionUtils.age(18, 65));
    }

    @FXML
    public void btnTemplateSex(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.SEX);
    }

    @FXML
    public void btnTemplateIdCard(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.ID_CARD);
    }

    @FXML
    public void btnTemplateQQ(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.QQ);
    }

    @FXML
    public void btnTemplateIP(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.IP);
    }

    @FXML
    public void btnTemplateEducation(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.EDUCATION);
    }

    @FXML
    public void btnTemplateJob(ActionEvent actionEvent){
        txtExpression.setText(ExpressionUtils.JOB);
    }
    // endregion=============================模板=================================================
}
