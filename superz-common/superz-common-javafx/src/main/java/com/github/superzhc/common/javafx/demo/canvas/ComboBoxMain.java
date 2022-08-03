package com.github.superzhc.common.javafx.demo.canvas;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/8/3 10:25
 **/
public class ComboBoxMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        ComboBox<String> cb = new ComboBox<>();
        cb.setItems(FXCollections.observableArrayList("11", "22", "33"));

        // 添加新值
        cb.getItems().add("44");
        // 可编辑
        cb.setEditable(true);
        cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(cb.getSelectionModel().getSelectedItem());
            }
        });

        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
