package com.github.superzhc.financial.desktop;

import com.github.superzhc.financial.desktop.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author superz
 * @create 2022/7/14 15:49
 **/
public class MainUIApp {
    public static void main(String[] args) {
        Application.launch(MainController.class, args);
    }
}
