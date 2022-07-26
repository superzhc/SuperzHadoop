package com.github.superzhc.common.javafx;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author superz
 * @create 2022/7/26 16:26
 **/
public class TableViewUtils {
    public static <T, S> TableColumn<T, S> numbers() {
        return numbers("序号");
    }

    public static <T, S> TableColumn<T, S> numbers(String title) {
        TableColumn<T, S> idColumn = new TableColumn<>(title);
        idColumn.setCellFactory(new Callback<TableColumn<T, S>, TableCell<T, S>>() {
            @Override
            public TableCell<T, S> call(TableColumn<T, S> param) {
                TableCell<T, S> cell = new TableCell<T, S>() {
                    @Override
                    protected void updateItem(S item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);

                        if (!empty) {
                            int rowIndex = this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;

            }
        });
        return idColumn;
    }
}
