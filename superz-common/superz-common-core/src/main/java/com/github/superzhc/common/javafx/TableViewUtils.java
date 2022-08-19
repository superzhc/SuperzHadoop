package com.github.superzhc.common.javafx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;

/**
 * @author superz
 * @create 2022/7/26 16:26
 **/
public class TableViewUtils {
    private static final Integer DEFAULT_COLUMN_LENGTH = 25;

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

    public static <T, S> TableColumn<T, S> checkbox(String title) {
        TableColumn<T, S> checkboxColumn = new TableColumn<>(title);
        checkboxColumn.setCellFactory(new Callback<TableColumn<T, S>, TableCell<T, S>>() {
            @Override
            public TableCell<T, S> call(TableColumn<T, S> param) {
                CheckBoxTableCell<T, S> cell = new CheckBoxTableCell<>(null, null);
                cell.setOnMouseClicked(event -> {
                    T value = (T) cell.getTableRow().getItem();
                    System.out.println(value);
                });
                return cell;
            }
        });
        return checkboxColumn;
    }

    public static <T, S> TableColumn<T, S> hyperlink(TableColumn<T, S> column) {
        column.setCellFactory(new Callback<TableColumn<T, S>, TableCell<T, S>>() {
            @Override
            public TableCell<T, S> call(TableColumn<T, S> param) {
                return new TableCell<T, S>() {
                    @Override
                    protected void updateItem(S item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null) {
                            super.setText(null);
                            super.setGraphic(null);
                        } else {
                            String url = (String) item;
                            Hyperlink link = new Hyperlink(url.length() > DEFAULT_COLUMN_LENGTH ? String.format("%s...", url.substring(0, DEFAULT_COLUMN_LENGTH)) : url);
                            link.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    if (Desktop.isDesktopSupported()) {
                                        // 桌面系统调用浏览器打开指定网址页面
                                        try {
                                            Desktop.getDesktop().browse(new URI(url));
                                        } catch (Exception e) {
                                            //ignore
                                        }
                                    }
                                }
                            });

                            super.setText(null);
                            super.setGraphic(link);
                        }
                    }
                };
            }
        });
        return column;
    }

    public static <T> TableView<Map<String, T>> clearAndBind(TableView<Map<String, T>> tv, List<Map<String, T>> data) {
        // 清除数据
        tv.setItems(null);

        // 清除列
        tv.getColumns().clear();

        return bind(tv, data);
    }

    public static <T> TableView<Map<String, T>> bind(TableView<Map<String, T>> tv, List<Map<String, T>> data) {
        // 可选中
        tv.getSelectionModel().setCellSelectionEnabled(true);
        tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE/*SelectionMode.MULTIPLE*/);

        // 新增菜单功能
        ContextMenu contextMenu = new ContextMenu();
        tv.setContextMenu(contextMenu);

        // 右击复制
        MenuItem copyMenu = new MenuItem("copy");
        copyMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TablePosition> positionList = tv.getSelectionModel().getSelectedCells();
                for (TablePosition position : positionList) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    ObservableValue observableValue = tv.getColumns().get(col).getCellObservableValue(row);
                    if (null != observableValue && observableValue.getValue() != null) {
                        // 复制到剪贴板
                        ClipboardContent clipboardContent = new ClipboardContent();
                        clipboardContent.putString(observableValue.getValue().toString());
                        Clipboard.getSystemClipboard().setContent(clipboardContent);
                    }
                }
            }
        });
        contextMenu.getItems().add(copyMenu);

//        // 保存菜单
//        Menu saveMenu = new Menu("Save");
//        MenuItem selectedRowSave = new MenuItem("Selected Row");
//        selectedRowSave.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                // 获取选中行
//                Map<String, T> data = tv.getSelectionModel().getSelectedItem();
//                DialogUtils.info(data.toString());
//            }
//        });
//        saveMenu.getItems().add(selectedRowSave);
//        contextMenu.getItems().add(saveMenu);


        // 绑定列
        List<TableColumn<Map<String, T>, T>> columns = createColumns(data);
        tv.getColumns().addAll(columns);

        // 绑定数据
        tv.setItems(FXCollections.observableList(data));
        return tv;
    }

    public static <T> List<TableColumn<Map<String, T>, T>> createColumns(List<Map<String, T>> data) {
        List<TableColumn<Map<String, T>, T>> columns = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0, len = data.size(); i < len; i++) {
            Map<String, T> row = data.get(i);
            for (Map.Entry<String, T> item : row.entrySet()) {
                String columnName = item.getKey();
                if (!columnNames.contains(columnName)) {
                    TableColumn<Map<String, T>, T> column = createColumn(columnName);
                    columns.add(column);
                    columnNames.add(columnName);
                }
            }
        }
        return columns;
    }

    public static <T> List<TableColumn<Map<String, T>, T>> createColumns(String... columnNames) {
        Set<String> names = new LinkedHashSet<>(Arrays.asList(columnNames));
        List<TableColumn<Map<String, T>, T>> columns = new ArrayList<>(names.size());
        for (String name : names) {
            columns.add(createColumn(name));
        }
        return columns;
    }

    public static <T> TableColumn<Map<String, T>, T> createColumn(String columnName) {
        TableColumn<Map<String, T>, T> column = new TableColumn<>(columnName);

        /**
         * 对表格中的每个单元格的值进行获取
         * 2022年8月10日 见 javafx.scene.control.cell.MapValueFactory 的实现，当时开发并不知道这个实现~~~
         */
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, T>, T>, ObservableValue<T>>() {
            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<Map<String, T>, T> param) {
                Map<String, T> currentRow = param.getValue();
                T value = currentRow.get(columnName);
                return new SimpleObjectProperty<T>(value);
            }
        });

        /**
         * 对表格中的每个单元格进行设置
         * 2022年8月10日 控制单元格中的数据过长，进行截断显示，并提供提示框显示全部
         */
        column.setCellFactory(new Callback<TableColumn<Map<String, T>, T>, TableCell<Map<String, T>, T>>() {
            @Override
            public TableCell<Map<String, T>, T> call(TableColumn<Map<String, T>, T> param) {
                final TableCell<Map<String, T>, T> cell = new TableCell<Map<String, T>, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        // 参考见 TableColumn.DEFAULT_CELL_FACTORY
                        if (item == getItem()) return;

                        super.updateItem(item, empty);

                        if (item == null) {
                            super.setText(null);
                            super.setGraphic(null);
                        } else if (item instanceof Node) {
                            super.setText(null);
                            super.setGraphic((Node) item);
                        } else if (item instanceof String) {
                            String str = (String) item;
                            if (null != str && str.length() > DEFAULT_COLUMN_LENGTH) {
                                super.setText(str.substring(0, DEFAULT_COLUMN_LENGTH) + "...");
                                Tooltip tooltip = new Tooltip();
                                tooltip.setPrefWidth(180.0);
                                tooltip.setText(str);
                                tooltip.setWrapText(true);
                                this.setTooltip(tooltip);
                            } else {
                                super.setText(str);
                            }
                            super.setGraphic(null);
                        } else {
                            super.setText(item.toString());
                            super.setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        // 2022年8月10日 通过setCellFactory已经控制了单列过长的问题
        //// 2022年8月4日 将宽度控制在一个范围
        //column.setMaxWidth(180);
        return column;
    }
}
