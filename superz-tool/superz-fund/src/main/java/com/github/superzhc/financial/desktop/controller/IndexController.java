package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.TableViewUtils;
import com.github.superzhc.financial.data.index.CSIndex;
import com.github.superzhc.financial.data.index.IndexData;
import com.github.superzhc.financial.data.index.SinaIndex;
import com.github.superzhc.tablesaw.utils.ConvertTableUtils;
import com.github.superzhc.financial.utils.DatasourceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @author superz
 * @create 2022/8/8 11:38
 **/
public class IndexController {

    @FXML
    private TextField txtIndexCode;

    @FXML
    private HBox container;

    @FXML
    public void btnGetComponent(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("请输入IndexCode");
            return;
        }

        String datasource = DialogUtils.choice("数据源", "请选择数据源", DatasourceType.Sina.getType(), DatasourceType.CS.getType());
        if (null == datasource) {
            return;
        }

        DatasourceType dt = DatasourceType.fromType(datasource);
        Table table = null;
        switch (dt) {
            case Sina:
                table = SinaIndex.stocks(indexCode);
                break;
            case CS:
                table = CSIndex.stocksWeight(indexCode);
                break;
        }

        container.getChildren().clear();
        if (null == table || table.isEmpty()) {
            DialogUtils.info("暂无数据");
            return;
        }

        TableView<Map<String, Object>> tv = new TableView<>();
        TableViewUtils.bind(tv, ConvertTableUtils.toMap(table));
        container.getChildren().add(tv);
    }

    @FXML
    public void btnGetHistory(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("请输入IndexCode");
            return;
        }

        container.getChildren().clear();
        Table table = IndexData.history(indexCode);
        if (null == table || table.isEmpty()) {
            DialogUtils.info("暂无数据");
            return;
        }

        TableView<Map<String, Object>> tv = new TableView<>();
        TableViewUtils.bind(tv, ConvertTableUtils.toMap(table));
        container.getChildren().add(tv);
    }

    @FXML
    public void btnGetHistroyChart(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("请输入IndexCode");
            return;
        }

        container.getChildren().clear();
        Table table = IndexData.history(indexCode);
        if (null == table || table.isEmpty()) {
            DialogUtils.info("暂无数据");
            return;
        }

        int size = table.rowCount();
        OHLCDataItem[] items = new OHLCDataItem[size];
        for (int i = 0; i < size; i++) {
            Row row = table.row(i);
            items[i] = new OHLCDataItem(
                    Date.from(row.getDate("date").atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    row.getDouble("open"),
                    row.getDouble("high"),
                    row.getDouble("low"),
                    row.getDouble("close"),
                    row.getInt("volume")
            );
        }

        DefaultOHLCDataset dataset = new DefaultOHLCDataset("Data", items);

        JFreeChart chart = ChartFactory.createCandlestickChart("Daily History", "date", null, dataset, true);

        ChartViewer viewer = new ChartViewer(chart);
        viewer.setPrefWidth(container.getPrefWidth() - container.getInsets().getLeft() - container.getInsets().getRight());
        viewer.setPrefHeight(container.getPrefHeight() - container.getInsets().getTop() - container.getInsets().getBottom());

        // 先清除再添加
        container.getChildren().clear();
        container.getChildren().add(viewer);
    }
}
