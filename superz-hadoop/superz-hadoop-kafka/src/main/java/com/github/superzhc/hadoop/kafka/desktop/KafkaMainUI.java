package com.github.superzhc.hadoop.kafka.desktop;

import com.github.superzhc.hadoop.kafka.desktop.controller.ToolKafkaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/27 17:16
 **/
public class KafkaMainUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String path = ToolKafkaController.FXML_PATH;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("工具");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}