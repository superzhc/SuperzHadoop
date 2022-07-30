package com.github.superzhc.ocr.desktop;

import com.github.superzhc.ocr.desktop.controller.ToolOCRController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/30 14:13
 **/
public class OCRMainUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ToolOCRController.getFxmlPath());
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
