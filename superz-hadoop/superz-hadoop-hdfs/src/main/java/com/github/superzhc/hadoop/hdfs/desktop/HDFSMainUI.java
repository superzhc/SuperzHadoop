package com.github.superzhc.hadoop.hdfs.desktop;

import com.github.superzhc.hadoop.hdfs.HdfsRestApi;
import com.github.superzhc.hadoop.hdfs.desktop.controller.ToolHdfsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author superz
 * @create 2022/8/22 15:52
 **/
public class HDFSMainUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(ToolHdfsController.FXML_PATH));
        Pane root = loader.load();
        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("工具");
        primaryStage.show();
    }
}
