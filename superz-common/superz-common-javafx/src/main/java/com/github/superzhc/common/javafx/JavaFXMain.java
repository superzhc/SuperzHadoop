package com.github.superzhc.common.javafx;

import com.github.superzhc.common.javafx.controller.ToolJavaCLIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

//        FXMLLoader loader = new FXMLLoader(ToolJavaCLIController.getFxmlPath());
//        root.getChildren().add(loader.load());

        Button btn = new Button("测试");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // DialogUtils.error(new IllegalArgumentException("非法参数"));
//                DialogUtils.prompt("消息","提示","100.0");
//                DialogUtils.alert("测试内容");

                HBox username = FormUtils.text("yyy");
                HBox password = FormUtils.text("xxx");

//                DialogUtils.form("表单",username,password);
            }
        });
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 600.0, 400.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("工具");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
