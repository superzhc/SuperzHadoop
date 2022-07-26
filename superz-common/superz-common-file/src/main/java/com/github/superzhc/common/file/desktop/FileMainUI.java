package com.github.superzhc.common.file.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/26 15:51
 **/
public class FileMainUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String path = "tool_file.fxml";

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
