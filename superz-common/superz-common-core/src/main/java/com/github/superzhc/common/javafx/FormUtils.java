package com.github.superzhc.common.javafx;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author superz
 * @create 2022/8/8 16:24
 **/
public class FormUtils {

    private static final Double SINGLE = 1.0 / 3;
    private static final Double DOUBLE = 1.0 / 7;

    public static HBox text(String text) {
        return text(text, null);
    }

    public static HBox text(String text, String value) {
        TextField textField = new TextField(value);
        return create(text, textField, SINGLE);
    }

    public static HBox textArea(String text) {
        return textArea(text, null);
    }

    public static HBox textArea(String text, String value) {
        TextArea textArea = new TextArea(value);
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
        double width = hbox.getPrefWidth();

        Label label = customLabel(text);
        double labelWidth = width / (1.0 + 1.0 / proportion);
        label.setPrefWidth(labelWidth);

        double controlWidth = width / (1.0 + proportion);
        control.setPrefWidth(controlWidth);

        hbox.getChildren().addAll(label, control);
        return hbox;
    }

    private static Label customLabel(String text) {
        Label lb = new Label(String.format("%s:", text));
        lb.setAlignment(Pos.CENTER_RIGHT);
        return lb;
    }
}
