package com.github.superzhc.desktop;

import com.github.superzhc.common.http.HttpRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

/**
 * @author superz
 * @create 2022/7/13 23:51
 */
public class Demo1Controller{

    public HTMLEditor htmlContent;
    public TextField txtUrl;

    @FXML
    protected void fetchHtml(ActionEvent actionEvent) {
        String url=txtUrl.getText();
        String result= HttpRequest.get(url).body();
        htmlContent.setHtmlText(result);
    }
}
