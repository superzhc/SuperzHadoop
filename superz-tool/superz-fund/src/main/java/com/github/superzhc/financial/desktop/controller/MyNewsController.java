package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.financial.desktop.control.utils.TablesawViewUtils;
import com.github.superzhc.financial.data.news.Jin10;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import tech.tablesaw.api.Table;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/20 9:49
 **/
public class MyNewsController implements Initializable {
    @FXML
    private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDateTime firstTime = LocalDateTime.now();
        Table table = Jin10.news(firstTime);
        TablesawViewUtils.bind(tableView, table);

        // Note：关闭了Tab，定时任务还是在继续执行
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            private LocalDateTime lastTime = firstTime;
//
//            @Override
//            public void run() {
//                LocalDateTime now = LocalDateTime.now();
//                Table table = Jin10.news(now);
//                table = table.where(table.dateTimeColumn("time").isAfter(lastTime));
//                TableViewUtils.append(tableView, table);
//                lastTime = now;
//            }
//        }, 1000 * 60, 1000 * 60 * 3);
    }
}
