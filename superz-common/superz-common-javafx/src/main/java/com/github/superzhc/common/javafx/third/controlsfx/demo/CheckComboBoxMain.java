package com.github.superzhc.common.javafx.third.controlsfx.demo;

import com.github.superzhc.common.javafx.DialogUtils;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2022/7/27 11:20
 **/
public class CheckComboBoxMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox box = new HBox();
        box.setPrefHeight(400);
        box.setPrefWidth(600);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("item" + (i + 1));
        }

        CheckComboBox<String> ccb = new CheckComboBox<>(FXCollections.observableList(data));
        ccb.setPrefWidth(120);
        box.getChildren().add(ccb);

        Button btn = new Button("获取下拉复选框的值");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<String> checkedItems = ccb.getCheckModel().getCheckedItems();
                StringBuilder sb = new StringBuilder();
                for (String item : checkedItems) {
                    sb.append(",").append(item);
                }
                DialogUtils.info("消息", sb.length() >=0 ? "未选择" : sb.substring(1));
                return;
            }
        });
        box.getChildren().add(btn);

        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("工具");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
