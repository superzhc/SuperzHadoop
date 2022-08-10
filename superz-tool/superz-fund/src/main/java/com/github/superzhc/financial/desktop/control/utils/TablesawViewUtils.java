package com.github.superzhc.financial.desktop.control.utils;

import com.github.superzhc.common.javafx.TableViewUtils;
import com.github.superzhc.tablesaw.utils.ConvertTableUtils;
import javafx.scene.control.TableView;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/7/18 16:03
 **/
public class TablesawViewUtils {
    public static TableView<Map<String, Object>> createTableView(Table table) {
        TableView<Map<String, Object>> tv = new TableView<>();
        return bind(tv, table);
    }

    public static TableView<Map<String, Object>> bind(TableView<Map<String, Object>> tv, Table table) {
        tv.getColumns().add(TableViewUtils.numbers("序号"));
        List<Map<String, Object>> dataRows = ConvertTableUtils.toMap(table);
        TableViewUtils.bind(tv, dataRows);
        return tv;
    }

    public static void append(TableView<Map<String, Object>> tv, Table table) {
        tv.getItems().addAll(0, ConvertTableUtils.toMap(table));
    }
}
