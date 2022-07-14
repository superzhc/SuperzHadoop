package com.github.superzhc.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/14 13:53
 **/
public class AppMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String appFxmlPath = "tablesaw.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(appFxmlPath));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("SUPERZ");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
