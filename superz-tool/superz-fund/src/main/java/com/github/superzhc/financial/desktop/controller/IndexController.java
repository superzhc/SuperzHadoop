package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.FormUtils;
import com.github.superzhc.financial.data.index.CSIndex;
import com.github.superzhc.financial.data.index.EastMoneyIndex;
import com.github.superzhc.financial.data.index.IndexData;
import com.github.superzhc.financial.data.index.SinaIndex;
import com.github.superzhc.financial.desktop.control.utils.TablesawViewUtils;
import com.github.superzhc.financial.utils.DatasourceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA;

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
    public void btnGetBasicInfo(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("提示", "请输入指数 Code");
            return;
        }

        container.getChildren().clear();

        String csIndexCode = indexCode.split("\\.")[0];
        // 指数基本信息
        String basicUrl = String.format("https://www.csindex.com.cn/csindex-home/indexInfo/index-basic-info/%s", csIndexCode);
        String basicResult = HttpRequest.get(basicUrl).userAgent(UA).body();
        Map<String, Object> basicMap = JsonUtils.map(basicResult, "data");

        HBox indexName = FormUtils.text("名称", basicMap.get("indexShortNameCn"));
        HBox indexType = FormUtils.text("类型", basicMap.get("indexType"));
        HBox publishDate = FormUtils.datePicker("发布时间", LocalDate.parse(String.valueOf(basicMap.get("publishDate")), DateTimeFormatter.ISO_LOCAL_DATE));
        HBox description = FormUtils.textArea("详情", basicMap.get("indexCnDesc"));


        GridPane pane = new GridPane();
        // pane.setAlignment(Pos.CENTER);
        pane.setPrefWidth(590.0);
        pane.setPrefHeight(285.0);
        // pane.getColumnConstraints()
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5, 5, 5, 5));
        indexName.setPrefWidth(280);
        pane.add(indexName, 0, 0);
        indexType.setPrefWidth(280);
        pane.add(indexType, 1, 0);
        publishDate.setPrefWidth(280);
        pane.add(publishDate, 0, 1);
        description.setPrefWidth(590);
        pane.add(description, 0, 2, 2, 2);
        container.getChildren().add(pane);
    }

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

        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
        container.getChildren().add(tv);
    }

    @FXML
    public void btnTranceIndex(ActionEvent actionEvent) {
        String indexCode = txtIndexCode.getText();
        if (null == indexCode || indexCode.trim().length() == 0) {
            DialogUtils.error("消息", "请输入指数Code");
            return;
        }

        container.getChildren().clear();
        Table table = EastMoneyIndex.tranceIndex(indexCode);
        if (null == table || table.isEmpty()) {
            DialogUtils.info("暂无数据");
            return;
        }

        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
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
        table = table.sortDescendingOn("date");

        TableView<Map<String, Object>> tv = TablesawViewUtils.createTableView(table);
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
