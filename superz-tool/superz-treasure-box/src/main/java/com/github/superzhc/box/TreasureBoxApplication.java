package com.github.superzhc.box;

import com.github.superzhc.common.javafx.Destroyable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreasureBoxApplication extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(TreasureBoxApplication.class);

    private static final String FRONT_PATH="/views";
    private static final String FRONT_INDEX_NAME="frame.fxml";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // 加载主窗口
        FXMLLoader mainLoader=new FXMLLoader(getClass().getResource(String.format("%s/%s",FRONT_PATH,FRONT_INDEX_NAME)));
        Parent root = mainLoader.load();
        // 通过场景设置窗口的大小
        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Treasure Box");
        // 窗口关闭事件
        primaryStage.setOnCloseRequest(this::doExit);

        primaryStage.show();
    }

    private void doExit(Event event) {
        // 退出窗口之前进行的操作，比如释放资源

        Platform.exit();
        System.exit(0);
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
