package com.github.superzhc.tool.desktop;

import com.github.superzhc.common.javafx.Destroyable;
import com.github.superzhc.tool.desktop.controller.ToolMainIndexController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author superz
 * @create 2022/8/19 9:19
 **/
public class ToolMainApplication extends Application {

    private static Stage stage;

    // 需要释放资源的资源列表
    private static List<Destroyable> destroyables = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(ToolMainIndexController.getFxmlPath());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setTitle("SUPERZ的工具箱");
        // 窗口关闭事件
        primaryStage.setOnCloseRequest(this::doExit);

        primaryStage.show();
    }

    private void doExit(Event event) {

        // 释放资源列表
        for (Destroyable destroyable : destroyables) {
            destroyable.destory();
        }

        Platform.exit();
        System.exit(0);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ToolMainApplication.stage = stage;
    }

    public static void addDestroy(Destroyable destroyable) {
        destroyables.add(destroyable);
    }

    public static void removeDestroy(Destroyable destroyable) {
        destroyables.remove(destroyable);
    }
}
