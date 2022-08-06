package com.github.superzhc.common.javafx.demo.control;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/8/3 10:08
 **/
public class ListViewMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                , "yyyyyy"
                , "zzzzzz"
                , "ssss"
        ));
        listView.getItems().add(0,"ffffffffff");
        listView.setPrefWidth(600);

        root.getChildren().add(listView);

        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}