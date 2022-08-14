package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.data.news.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/10 10:22
 **/
public class NewsController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    public static enum NewsSource {

        Jin10("金十数据"), _36kr("36kr"), WallStreet(), WeiXin(0), WeiBo, ZhiHu, AnyKnew, CaiLianShe("财联社");

        private int status = 1;
        private String sourceName = null;

        NewsSource() {
        }

        NewsSource(int status) {
            this.status = status;
        }

        NewsSource(String sourceName) {
            this.sourceName = sourceName;
        }

        NewsSource(String sourceName, int status) {
            this.status = status;
            this.sourceName = sourceName;
        }

        public boolean isAvailable() {
            return this.status == 1;
        }

        public String getSourceName() {
            return sourceName == null || sourceName.trim().length() == 0 ? name() : sourceName;
        }

        public static String[] availableNewsSource() {
            NewsSource[] sources = values();

            List<String> availableSource = new ArrayList<>();
            for (NewsSource source : sources) {
                if (source.isAvailable()) {
                    availableSource.add(source.getSourceName());
                }
            }

            return availableSource.toArray(new String[availableSource.size()]);
        }

        public static NewsSource fromSourceName(String sourceName) {
            NewsSource[] sources = values();
            for (NewsSource source : sources) {
                if (source.getSourceName().equals(sourceName)) {
                    return source;
                }
            }
            return null;
        }
    }

    @FXML
    private ChoiceBox<String> cbSources;

    @FXML
    private TableView<Map<String, Object>> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSources.getItems().addAll(NewsSource.availableNewsSource());
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

        NewsSource newsSource = NewsSource.fromSourceName(source);
        switch (newsSource) {
            case Jin10:
                table = Jin10.news();
                break;
            case WallStreet:
                table = WallStreet.news();
                break;
            case WeiXin:
                table = BJSouBang.weixinHot();
                break;
            case WeiBo:
                table = AnyKnew.weibo();
                break;
            case ZhiHu:
                table = AnyKnew.zhihu();
                break;
            case AnyKnew:
                table = AnyKnew.finance();
                break;
            case CaiLianShe:
                table = CailianShe.telegraph();
                break;
            case _36kr:
                table = News36kr.latest();
                break;
        }
        return table;
    }
}
