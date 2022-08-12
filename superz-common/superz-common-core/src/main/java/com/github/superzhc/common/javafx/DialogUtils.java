package com.github.superzhc.common.javafx;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * @author superz
 * @create 2022/7/13 17:39
 **/
public class DialogUtils {
    private static final String DEFAULT_TITLE = "消息";

//    public static void alert(String title, String content) {
//        Alert alert = new Alert(AlertType.NONE);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//
//        alert.showAndWait();
//    }

    public static void alert(String content) {
        info(content);
    }

    public static void info(String content) {
        info(DEFAULT_TITLE, null, content);
    }

    public static void info(String title, String content) {
        info(title, null, content);
    }

    public static void info(String title, String headerText, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        // 需要设置为null，不然会有默认值
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void warning(String content) {
        warning(DEFAULT_TITLE, content);
    }

    public static void warning(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void error(String content) {
        error(DEFAULT_TITLE, content);
    }

    public static void error(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void error(Throwable throwable) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(DEFAULT_TITLE);
        alert.setHeaderText("发生异常");

        // 获取错误异常的信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        AnchorPane pane = new AnchorPane(textArea);
        alert.getDialogPane().setExpandableContent(pane);

        alert.showAndWait();
    }

    public static boolean confirm(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return ButtonType.OK == result.get();
    }

    public static String prompt(String title, String msg) {
        return prompt(title, msg, null);
    }

    public static String prompt(String title, String msg, String defaultInput) {
        TextInputDialog dialog = new TextInputDialog(defaultInput);
        dialog.setTitle(title);
        // dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText(msg);

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static <T> T choice(String title, String msg, T... values) {
        ChoiceDialog<T> dialog = new ChoiceDialog<T>(null, values);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(msg);

        Optional<T> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
