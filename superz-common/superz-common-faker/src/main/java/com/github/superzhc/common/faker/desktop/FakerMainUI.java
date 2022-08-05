package com.github.superzhc.common.faker.desktop;

import com.github.superzhc.common.faker.desktop.controller.ToolFakerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/8/5 17:38
 **/
public class FakerMainUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ToolFakerController.getFxmlPath());
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
