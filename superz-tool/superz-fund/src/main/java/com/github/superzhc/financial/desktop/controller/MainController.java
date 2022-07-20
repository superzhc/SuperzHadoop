package com.github.superzhc.financial.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/15 1:35
 */
public class MainController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @FXML
    private MenuBar menu;

    @FXML
    private TabPane contentTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            /**
             * 菜单逻辑
             */
            log.info("初始化菜单开始...");
            JsonNode json = JsonUtils.file(getClass().getResource("/menu.json").getPath());
            Iterator<String> ite = json.fieldNames();
            while (ite.hasNext()) {
                String menuName = ite.next();
                // 一级菜单的类型必须为 Menu
                MenuItem menuItem = parse(json.get(menuName));
                if (!(menuItem instanceof Menu)) {
                    //throw new RuntimeException("顶层菜单的hasChild属性必须为true");
                    // 跳过配置错误的顶层菜单
                    log.error("顶层菜单的hasChild属性必须为true，且isSeparator不可设置为true");
                    continue;
                }
                menu.getMenus().add((Menu) menuItem);
            }
            log.info("菜单生成完成！！！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MenuItem parse(JsonNode node) {
        String name = JsonUtils.string(node, "name");
        boolean hasChild = JsonUtils.bool(node, "hasChild");
        boolean isSeparator = JsonUtils.bool(node, "isSeparator");
        if (isSeparator) {
            SeparatorMenuItem separator = new SeparatorMenuItem();
            return separator;
        } else if (hasChild) {
            // 定义当前节点
            Menu menuInfo = new Menu(name);

            JsonNode children = node.get("children");
            Iterator<String> childrenName = children.fieldNames();
            while (childrenName.hasNext()) {
                JsonNode childNode = children.get(childrenName.next());
                MenuItem menuItem = parse(childNode);
                menuInfo.getItems().add(menuItem);
            }
            return menuInfo;
        } else {
            MenuItem menuItem = new MenuItem(name);

            String title = JsonUtils.string(node, "title");
            String url = JsonUtils.string(node, "url");
            menuItem.setOnAction(event -> {
                try {
                    // 判断是否存在已打开的 Tab，若已打开不在重新新增Tab
                    ObservableList<Tab> openedTabs = contentTab.getTabs();
                    for (Tab openedTab : openedTabs) {
                        if (title.equals(openedTab.getText())) {
                            contentTab.getSelectionModel().select(openedTab);
                            return;
                        }
                    }

                    Tab tab = new Tab(title);
                    tab.setContent(FXMLLoader.load(getClass().getResource(url)));
                    contentTab.getTabs().add(tab);
                    contentTab.getSelectionModel().select(tab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return menuItem;
        }
    }
}
