package com.github.superzhc.common.javafx;

import com.github.superzhc.common.javafx.controller.ToolJavaCLIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/27 11:19
 **/
public class JavaFXMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        FXMLLoader loader = new FXMLLoader(ToolJavaCLIController.getFxmlPath());
        root.getChildren().add(loader.load());

        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("工具");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
