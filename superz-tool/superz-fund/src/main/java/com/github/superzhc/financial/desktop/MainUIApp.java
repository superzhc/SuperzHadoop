package com.github.superzhc.financial.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/14 15:49
 **/
public class MainUIApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String appFxmlPath = "view/index_info.fxml";
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
