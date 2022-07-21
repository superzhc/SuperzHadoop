package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.financial.data.index.EastMoneyIndex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/21 14:44
 **/
public class IndexHistoryChartController implements Initializable {
    @FXML
    private TextField txtIndexCode;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private HBox container;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void chgStart(ActionEvent actionEvent) {
        final LocalDate start = dpStart.getValue();
        dpEnd.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (null != start && item.isBefore(start.plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    public void chgEnd(ActionEvent actionEvent) {
        final LocalDate end = dpEnd.getValue();
        dpStart.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (null != end && item.isAfter(end.minusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    public void btnSearch(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Code");
            return;
        }

        Table table = EastMoneyIndex.dailyHistory(indexCode);

        LocalDate start = dpStart.getValue();
        if (null != start) {
            table = table.where(table.dateColumn("date").isAfter(start));
        }
        LocalDate end = dpEnd.getValue();
        if (null != end) {
            table = table.where(table.dateColumn("date").isBefore(end));
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
