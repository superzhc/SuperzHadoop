package com.github.superzhc.common.javafx;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/8/8 16:24
 **/
public class FormUtils {

    private static final Double SINGLE = 1.0 / 3;
    private static final Double DOUBLE = 1.0 / 7;

    public static GridPane layout(double width, double height) {
        return layout(width, height, 2);
    }

    public static GridPane layout(double width, double height, int columnCount) {
        GridPane pane = new GridPane();
        pane.setPrefWidth(width);
        pane.setPrefHeight(height);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5, 5, 5, 5));

        // 列配置
        for (int i = 0; i < columnCount; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            // 每列的占比宽度相同
            constraints.setPercentWidth(100.0 / columnCount);
            // constraints.setPrefWidth(width / columnCount);
            constraints.setFillWidth(true);
            pane.getColumnConstraints().add(constraints);
        }

        // 行配置
        // RowConstraints rowConstraints=new RowConstraints();
        // rowConstraints.setPrefHeight(35.0);

        return pane;
    }

    public static GridPane addCell(GridPane gridPane, HBox cell, int columnIndex, int rowIndex) {
        // 设置每个子件的宽度
        cell.prefWidthProperty().bind(gridPane.prefWidthProperty().multiply(gridPane.getColumnConstraints().get(columnIndex).getPercentWidth() / 100.0));
        gridPane.add(cell, columnIndex, rowIndex);
        return gridPane;
    }

    public static HBox text(String text) {
        return text(text, null);
    }

    public static HBox text(String text, Object value) {
        TextField textField = new TextField(String.valueOf(value));
        return create(text, textField, SINGLE);
    }

    public static HBox textArea(String text) {
        return textArea(text, null);
    }

    public static HBox textArea(String text, Object value) {
        TextArea textArea = new TextArea(String.valueOf(value));
        // 自动换行
        textArea.setWrapText(true);
        return create(text, textArea, DOUBLE);
    }

    public static <T> HBox choiceBox(String text, T defaultValue, T... values) {
        ChoiceBox<T> cb = new ChoiceBox<T>(FXCollections.observableArrayList(values));
        if (null != defaultValue) {
            cb.setValue(defaultValue);
        }
        return create(text, cb, SINGLE);
    }

    public static HBox datePicker(String text, LocalDate date) {
        DatePicker dp = new DatePicker();
        dp.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (null == object) {
                    return "";
                }
                return object.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }

            @Override
            public LocalDate fromString(String string) {
                if (null == string || string.trim().length() == 0) {
                    return null;
                } else {
                    return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE);
                }
            }
        });
        dp.setValue(date);
        return create(text, dp, SINGLE);
    }

    public static HBox create(String text, Control control, double proportion) {
        HBox hbox = new HBox();
        hbox.setSpacing(1.0);
        // double width = hbox.getPrefWidth();

        Label label = customLabel(text);
        // label.setPrefHeight(25.0);
        // double labelWidth = width / (1.0 + 1.0 / proportion);
        // label.setPrefWidth(labelWidth);
        // 控件宽度受父元素的控制
        label.prefHeightProperty().bind(hbox.heightProperty().subtract(10.0));
        label.prefWidthProperty().bind(hbox.prefWidthProperty().divide(1.0 + 1.0 / proportion));

        // control.setPrefHeight(25.0);
        // double controlWidth = width / (1.0 + proportion);
        // control.setPrefWidth(controlWidth);
        control.prefHeightProperty().bind(hbox.heightProperty().subtract(10.0));
        control.prefWidthProperty().bind(hbox.prefWidthProperty().divide(1.0 + proportion));

        hbox.getChildren().addAll(label, control);
        return hbox;
    }

    private static Label customLabel(String text) {
        Label lb = new Label(String.format("%s：", text));
        lb.setAlignment(Pos.CENTER_RIGHT);
        return lb;
    }
}
