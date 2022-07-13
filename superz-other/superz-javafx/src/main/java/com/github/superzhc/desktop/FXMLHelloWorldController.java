package com.github.superzhc.desktop;

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
        //System.out.println(counter.getAndIncrement());
        myCounter.setText(String.valueOf(counter.getAndIncrement()));
    }
}
