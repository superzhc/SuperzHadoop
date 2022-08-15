package com.github.superzhc.common.javafx;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author superz
 * @create 2022/7/13 17:39
 **/
public class DialogUtils {
    private static final String DEFAULT_TITLE = "消息";

    public static void alert(String content) {
        alert(DEFAULT_TITLE, content);
    }

    public static void alert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
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
        alert.setContentText(throwable.getLocalizedMessage());

        // 获取错误异常的信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    public static boolean confirm(String content) {
        return confirm(DEFAULT_TITLE, content);
    }

    public static boolean confirm(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return ButtonType.OK == result.get();
    }

    public static String prompt(String msg) {
        return prompt(null, msg, null);
    }

    public static String prompt(String msg, String defaultValue) {
        return prompt(null, msg, defaultValue);
    }

    public static String prompt(String title, String msg, String defaultInput) {
        TextInputDialog dialog = new TextInputDialog(defaultInput);
        dialog.setTitle(title);
        dialog.setGraphic(null);
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
        dialog.setGraphic(null);
        dialog.setHeaderText(null);
        dialog.setContentText(msg);

        Optional<T> result = dialog.showAndWait();
        return result.orElse(null);
    }

//    public static void form(String title, HBox... boxs) {
//        Dialog<Map<String,Object>> dialog = new Dialog<Map<String,Object>>();
//        dialog.setTitle(title);
//
//        final DialogPane dialogPane = dialog.getDialogPane();
//        final GridPane gridPane = FormUtils.layout(300, 300, 1);
//        for (int i = 0, len = boxs.length; i < len; i++) {
//            FormUtils.addCell(gridPane, boxs[i], 0, i);
//        }
//
//        dialogPane.setContent(gridPane);
//        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        dialog.setResultConverter(new Callback<ButtonType, Map<String,Object>>() {
//            @Override
//            public Map<String,Object> call(ButtonType buttonType) {
//                ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
//                if (data == ButtonBar.ButtonData.OK_DONE) {
//                    Map<String, Object> map = new HashMap<>();
//
//                    ObservableList<Node> nodes = gridPane.getChildren();
//                    for (Node node : nodes) {
//                        if (node instanceof HBox) {
//                            HBox box = (HBox) node;
//                            Pair<String, Object> pair = FormUtils.parse(box);
//                            map.put(pair.getKey(), pair.getValue());
//                        }
//                    }
//                    return map;
//                }
//                return null;
//            }
//        });
//
//        dialog.showAndWait();
//    }
}
