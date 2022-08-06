package com.github.superzhc.common.javafx.demo.service;

import com.github.superzhc.common.javafx.TableViewUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * @author superz
 * @create 2022/8/6 14:43
 **/
public class TimerMain extends Application {

    Label label;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        // Label label=new Label(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        HBox box = new HBox();
        box.setPrefHeight(100);
        box.setPrefWidth(590);

        label = new Label();
        label.setPrefWidth(200);
        label.setPrefHeight(50);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // UI 更新需要用如下方法：
                Platform.runLater(() -> {
                    label.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);

        box.getChildren().add(label);
        root.getChildren().add(box);

        HBox box2 = new HBox();
        box.setPrefHeight(100);
        box.setPrefWidth(590);
        root.getChildren().add(box2);

        TableView<Map<String, Object>> tv = new TableView<>();
        tv.setPrefHeight(100);
        tv.setPrefWidth(590);
        tv.getColumns().addAll(TableViewUtils.createColumns("id", "name"));
        box2.getChildren().add(tv);

        Timer timer2 = new Timer();
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                // 注意事项：跟label更新做区别，数据更新不涉及组件更新，无需使用Platform.runLater
                List<Map<String, Object>> data = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    Long number = System.currentTimeMillis();
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", number + i);
                    row.put("name", String.format("superz_%d", (number + i)));
                    data.add(row);
                }
                tv.getItems().addAll(data);
            }
        };
        timer2.schedule(timerTask2, 1000 * 5, 1000 * 3);

        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("定时任务");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
