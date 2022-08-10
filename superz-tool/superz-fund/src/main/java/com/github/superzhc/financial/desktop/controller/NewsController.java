package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.data.news.AnyKnew;
import com.github.superzhc.financial.data.news.BJSouBang;
import com.github.superzhc.financial.data.news.Jin10;
import com.github.superzhc.financial.data.news.WallStreet;
import com.github.superzhc.financial.desktop.control.utils.TablesawViewUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/10 10:22
 **/
public class NewsController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);
    @FXML
    private ChoiceBox<String> cbSources;

    @FXML
    private TableView<Map<String, Object>> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSources.getItems().addAll("Jin10", "WallStreet", "WeiXin", "WeiBo", "ZhiHu", "XueQiu", "AnyKnew");
        cbSources.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String source = cbSources.getItems().get(newValue.intValue());
                log.debug("切换 News 源：{}", source);
                bindTableView(source);
            }
        });
    }

    @FXML
    public void btnRefresh(ActionEvent actionEvent) {
        String source = cbSources.getValue();
        log.debug("刷新 News 源：{}", source);
        bindTableView(source);
    }

    private void bindTableView(String source) {
        // 先清除原有的数据和列
        tableView.getItems().clear();
        tableView.getColumns().clear();

        Table table = getData(source);
        if (null != table) {
            TablesawViewUtils.bind(tableView, table);
        }
    }

    private Table getData(String source) {
        Table table = null;
        switch (source) {
            case "Jin10":
                table = Jin10.news();
                break;
            case "WallStreet":
                table = WallStreet.news();
                break;
            case "WeiXin":
                table = BJSouBang.weixinHot();
                break;
            case "WeiBo":
                table = AnyKnew.weibo();
                break;
            case "ZhiHu":
                table = AnyKnew.zhihu();
                break;
            case "XueQiu":
                table = AnyKnew.xueqiu();
                break;
            case "AnyKnew":
                table = AnyKnew.finance();
                break;
        }
        return table;
    }
}
