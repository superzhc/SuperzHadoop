package com.github.superzhc.hadoop.es.desktop;

import com.github.superzhc.hadoop.es.desktop.controller.ToolESController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/27 15:00
 **/
public class ESMainUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String path = ToolESController.FXML_PATH;

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
