package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.data.news.News36kr;
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
 * @create 2022/8/19 16:43
 **/
public class News36krController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(News36krController.class);

    private static final String NEWS_36KR_CATEGORY_LATEST = "最新";
    private static final String NEWS_36KR_CATEGORY_RECOMMEND = "推荐";
    private static final String NEWS_36KR_CATEGORY_VC = "VC";
    private static final String NEWS_36KR_CATEGORY_CAR = "汽车";
    private static final String NEWS_36KR_CATEGORY_TECHNOLOGY = "科技";
    private static final String NEWS_36KR_CATEGORY_ES = "企业服务";
    private static final String NEWS_36KR_CATEGORY_INNOVATE = "创新";
    private static final String NEWS_36KR_CATEGORY_RE = "房产";
    private static final String NEWS_36KR_CATEGORY_LIFE = "生活";
    private static final String NEWS_36KR_CATEGORY_OTHER = "其他";
    private static final String NEWS_36KR_CATEGORY_FINANCE = "金融";

    @FXML
    private ChoiceBox<String> cbSources;

    @FXML
    private TableView<Map<String, Object>> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSources.getItems().addAll(
                NEWS_36KR_CATEGORY_LATEST
                , NEWS_36KR_CATEGORY_RECOMMEND
                , NEWS_36KR_CATEGORY_FINANCE
                , NEWS_36KR_CATEGORY_VC
                , NEWS_36KR_CATEGORY_CAR
                , NEWS_36KR_CATEGORY_TECHNOLOGY
                , NEWS_36KR_CATEGORY_ES
                , NEWS_36KR_CATEGORY_INNOVATE
                , NEWS_36KR_CATEGORY_RE
                , NEWS_36KR_CATEGORY_LIFE
                , NEWS_36KR_CATEGORY_OTHER
        );
        cbSources.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String source = cbSources.getItems().get(newValue.intValue());
                log.debug("切换分类：{}", source);
                bindTableView(source);
            }
        });
    }

    @FXML
    public void btnRefresh(ActionEvent actionEvent) {
        String source = cbSources.getValue();
        log.debug("刷新分类：{}", source);
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

    private Table getData(String category) {
        Table table = null;

        switch (category) {
            case NEWS_36KR_CATEGORY_RECOMMEND:
                table = News36kr.recommend();
                break;
            case NEWS_36KR_CATEGORY_FINANCE:
                table = News36kr.finance();
                break;
            case NEWS_36KR_CATEGORY_VC:
                table = News36kr.vc();
                break;
            case NEWS_36KR_CATEGORY_CAR:
                table = News36kr.car();
                break;
            case NEWS_36KR_CATEGORY_TECHNOLOGY:
                table = News36kr.technology();
                break;
            case NEWS_36KR_CATEGORY_ES:
                table = News36kr.enterpriseService();
                break;
            case NEWS_36KR_CATEGORY_INNOVATE:
                table = News36kr.innovate();
                break;
            case NEWS_36KR_CATEGORY_RE:
                table = News36kr.realEstate();
                break;
            case NEWS_36KR_CATEGORY_LIFE:
                table = News36kr.life();
                break;
            case NEWS_36KR_CATEGORY_OTHER:
                table = News36kr.other();
                break;
            case NEWS_36KR_CATEGORY_LATEST:
            default:
                table = News36kr.latest();
                break;
        }

        return table;
    }
}
