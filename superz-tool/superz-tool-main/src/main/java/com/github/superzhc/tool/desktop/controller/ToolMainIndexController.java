package com.github.superzhc.tool.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.javafx.Destroyable;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.tool.desktop.ToolMainApplication;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/19 9:58
 **/
public class ToolMainIndexController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(ToolMainIndexController.class);

    private static final String FXML_PATH = "/com/github/superzhc/tool/desktop/view/main.fxml";

    public static URL getFxmlPath() {
        return ToolMainIndexController.class.getResource(FXML_PATH);
    }

    @FXML
    private MenuBar menu;

    @FXML
    private TabPane contentTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JsonNode json = JsonUtils.file(getClass().getResource("/menu.json").getPath());

        // 一级菜单配置
        Iterator<String> ite = json.fieldNames();
        while (ite.hasNext()) {
            String key = ite.next();
            JsonNode menuConfig = JsonUtils.object(json, key);

            String menuName = JsonUtils.string(menuConfig, "name");
            boolean hasChild = JsonUtils.bool(menuConfig, "hasChild");
            boolean isSeparator = JsonUtils.bool(menuConfig, "isSeparator");
            // 顶层菜单的hasChild属性必须为true，且isSeparator不可设置为true
            if (!hasChild) {
                log.error("一级菜单[{}]必须要有子节点", menuName);
                continue;
            }
            if (isSeparator) {
                log.error("一级菜单不可谓分隔符");
                continue;
            }

            Menu oneLevelMenu = new Menu(menuName);

            for (String subKey : JsonUtils.objectKeys(menuConfig, "children")) {
                MenuItem subMenu = parse(JsonUtils.object(menuConfig, "children", subKey));
                oneLevelMenu.getItems().add(subMenu);
            }

            menu.getMenus().add(oneLevelMenu);
        }
        log.info("加载菜单完成!");
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
            String controller = JsonUtils.string(node, "controller");
            String path = JsonUtils.string(node, "path");
            boolean isNeedRelease = JsonUtils.bool(node, "isNeedRelease");
            menuItem.setOnAction(event -> {
                if (null == path || path.trim().length() == 0) {
                    DialogUtils.warning("未配置页面地址");
                    return;
                }

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
                    URL url = Class.forName(controller).getClassLoader().getResource(path);
                    log.debug("打开页面：{}", url);
                    FXMLLoader loader = new FXMLLoader(url);
                    tab.setContent(loader.load());

                    if (isNeedRelease) {
                        Destroyable destroyable = loader.getController();
                        ToolMainApplication.addDestroy(destroyable);
                        tab.setOnClosed(event1 -> {
                            destroyable.destory();
                            ToolMainApplication.removeDestroy(destroyable);
                        });
                    }

                    contentTab.getTabs().add(tab);
                    contentTab.getSelectionModel().select(tab);
                } catch (Exception e) {
                    DialogUtils.error("打开页面失败", e);
                }
            });
            return menuItem;
        }
    }
}
