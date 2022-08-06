package com.github.superzhc.common.javafx.demo.control;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * 右键菜单
 *
 * @author superz
 * @create 2022/8/6 15:40
 **/
public class ContextMenuMain extends Application {

    /**
     * Stage：就是你能看到的整个软件界面（窗口）
     * Scene：就是除了窗口最上面有最大、最小化及关闭按钮那一行及窗口边框外其它的区域（场景）
     * 场景（Scene）是一个窗口（Stage）必不可少的
     */
    @Override
    public void start(Stage stage) throws Exception {
        final Label label = new Label("一个即将被添加右键菜单的Label");
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-background-color:gray");

        // 右键菜单===================================================================
        // 创建右键菜单
        ContextMenu contextMenu = new ContextMenu();
        // 菜单项
        MenuItem redBg = new MenuItem("黑底白色");
        redBg.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                label.setStyle("-fx-background-color:black;-fx-text-fill:white;");
            }
        });
        // 菜单项
        MenuItem blueBg = new MenuItem("白底黑字");
        blueBg.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                label.setStyle("-fx-background-color:white;-fx-text-fill:black;");
            }
        });
        contextMenu.getItems().addAll(redBg, blueBg);
        // 右键菜单===================================================================

        // 添加右键菜单到label
        label.setContextMenu(contextMenu);

        // 1、初始化一个场景
        Scene scene = new Scene(label, 800, 600);
        // 2、将场景放入窗口
        stage.setScene(scene);
        // 3、打开窗口
        stage.show();
    }

    public static void main(String[] args) {
        // 启动软件
        Application.launch(args);
    }
}
