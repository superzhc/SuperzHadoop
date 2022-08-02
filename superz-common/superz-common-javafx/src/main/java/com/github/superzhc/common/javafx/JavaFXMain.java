package com.github.superzhc.common.javafx;

import javafx.application.Application;
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
        Group root=new Group();



        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
