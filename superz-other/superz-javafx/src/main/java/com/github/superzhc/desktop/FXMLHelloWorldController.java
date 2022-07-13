package com.github.superzhc.desktop;

import com.github.superzhc.desktop.dialog.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superz
 * @create 2022/7/13 14:37
 **/
public class FXMLHelloWorldController {
    private static AtomicInteger counter = new AtomicInteger();

    @FXML
    private Text myCounter;


    @FXML
    protected void handleBtnEvent(ActionEvent actionEvent) {
        int i = counter.getAndIncrement();
        DialogUtils.info("提示", String.valueOf(i));
        myCounter.setText(String.valueOf(i));
    }
}
