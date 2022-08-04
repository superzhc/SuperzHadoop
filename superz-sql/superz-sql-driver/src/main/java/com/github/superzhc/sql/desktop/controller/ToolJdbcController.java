package com.github.superzhc.sql.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.TableViewUtils;
import com.github.superzhc.common.jdbc.JdbcHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/8/4 9:15
 **/
public class ToolJdbcController {
    public static final String FXML_PATH = "../../view/tool_jdbc.fxml";

    public static URL getFxmlPath() {
        return ToolJdbcController.class.getResource(FXML_PATH);
    }

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnConnect;
    @FXML
    private Button btnDisconnect;

    @FXML
    private ComboBox<String> cbCatalog;

    @FXML
    private ComboBox<String> cbSchema;

    @FXML
    private ComboBox<String> cbTable;

    @FXML
    private TextArea txtSQL;

    @FXML
    private TableView<Map<String, Object>> tvResult;

    private boolean isConnect = false;
    private JdbcHelper jdbc = null;

    @FXML
    public void btnConnectAction(ActionEvent actionEvent) {
        String url = txtUrl.getText();
        if (null == url || url.trim().length() == 0) {
            DialogUtils.error("请输入连接地址");
            return;
        }

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            jdbc = new JdbcHelper(url, username, password);
            jdbc.getConnection();
            isConnect = true;

            String[] catalogs = jdbc.catalogs();
            cbCatalog.setItems(FXCollections.observableArrayList(catalogs));

            String[] schemas = jdbc.schemas();
            cbSchema.setItems(FXCollections.observableArrayList(schemas));

            txtUrl.setDisable(true);
            txtUsername.setDisable(true);
            txtPassword.setDisable(true);
            btnConnect.setDisable(true);
            btnDisconnect.setDisable(false);

            DialogUtils.info("连接成功");
        } catch (Exception e) {
            DialogUtils.error("连接失败：" + e.getMessage());
        }
    }

    @FXML
    public void btnDisconnectAction(ActionEvent actionEvent) {
        try {
            jdbc.close();
            isConnect = false;

            txtUrl.setDisable(false);
            txtUsername.setDisable(false);
            txtPassword.setDisable(false);
            btnConnect.setDisable(false);
            btnDisconnect.setDisable(false);

            cbCatalog.setItems(null);
            cbSchema.setItems(null);
            cbTable.setItems(null);

            DialogUtils.info("关闭连接成功");
        } catch (Exception e) {
            DialogUtils.error("断开连接失败：" + e.getMessage());
        }
    }

    @FXML
    public void btnGetCatalogAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        String[] catalogs = jdbc.catalogs();
        cbCatalog.setItems(FXCollections.observableArrayList(catalogs));

        try {
            cbCatalog.setValue(jdbc.getConnection().getCatalog());
        } catch (Exception e) {
            //ignore
        }

        DialogUtils.info("获取成功");
    }

    @FXML
    public void btnGetSchemaAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        String[] schemas = jdbc.schemas();
        cbSchema.setItems(FXCollections.observableArrayList(schemas));

        try {
            cbSchema.setValue(jdbc.getConnection().getSchema());
        } catch (SQLException e) {
            //ignore
        }

        DialogUtils.info("获取成功");
    }

    @FXML
    public void btnGetTableAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        try {
            String catalog = cbCatalog.getValue();
            if (null != catalog && catalog.trim().length() > 0) {
                jdbc.getConnection().setCatalog(catalog);
            }

            String schema = cbSchema.getValue();
            if (null != schema && schema.trim().length() > 0) {
                jdbc.getConnection().setSchema(schema);
            }
            String[] tables = jdbc.tables();
            cbTable.setItems(FXCollections.observableArrayList(tables));

            DialogUtils.info("获取成功");
        } catch (Exception e) {
            DialogUtils.error(e.getMessage());
        }
    }

    @FXML
    public void btnGetColumnsAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        String table = cbTable.getValue();
        if (null == table || table.trim().length() == 0) {
            DialogUtils.error("请选择表");
            return;
        }


        try {
            String catalog = cbCatalog.getValue();
            if (null != catalog && catalog.trim().length() > 0) {
                jdbc.getConnection().setCatalog(catalog);
            }

            String schema = cbSchema.getValue();
            if (null != schema && schema.trim().length() > 0) {
                jdbc.getConnection().setSchema(schema);
            }

            List<Map<String, String>> columns = jdbc.columns(table);
            if (null != columns) {
                List<Map<String, Object>> columns2 = new ArrayList<>();
                for (Map<String, String> column : columns) {
                    Map<String, Object> column2 = new LinkedHashMap<>();
                    for (Map.Entry<String, String> item : column.entrySet()) {
                        column2.put(item.getKey(), item.getValue());
                    }
                    columns2.add(column2);
                }

                TableViewUtils.clearAndBind(tvResult, columns2);
            }
        } catch (Exception e) {
            DialogUtils.error(e.getMessage());
        }
    }

    @FXML
    public void btnPreviewAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        String table = cbTable.getValue();
        if (null == table || table.trim().length() == 0) {
            DialogUtils.error("请选择表");
            return;
        }

        try {
            String catalog = cbCatalog.getValue();
            if (null != catalog && catalog.trim().length() > 0) {
                jdbc.getConnection().setCatalog(catalog);
            }

            String schema = cbSchema.getValue();
            if (null != schema && schema.trim().length() > 0) {
                jdbc.getConnection().setSchema(schema);
            }

            JdbcHelper.Page page = new JdbcHelper.Page(jdbc, table, 100);
            List<Map<String, Object>> data = jdbc.query(page.sql());
            if (null != data) {
                TableViewUtils.clearAndBind(tvResult, data);
            }
        } catch (Exception e) {
            DialogUtils.error(e.getMessage());
        }
    }

    /**
     * SQL 查询语句
     *
     * @param actionEvent
     */
    @FXML
    public void btnDQLAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("请先连接JDBC");
            return;
        }

        String sql = txtSQL.getText();
        if (null == sql || sql.trim().length() == 0) {
            DialogUtils.error("请输入查询语句");
            return;
        }

        try {
            String catalog = cbCatalog.getValue();
            if (null != catalog && catalog.trim().length() > 0) {
                jdbc.getConnection().setCatalog(catalog);
            }

            String schema = cbSchema.getValue();
            if (null != schema && schema.trim().length() > 0) {
                jdbc.getConnection().setSchema(schema);
            }

            List<Map<String, Object>> data = jdbc.query(sql);
            if (null != data) {
                TableViewUtils.clearAndBind(tvResult, data);
            }
        } catch (Exception e) {
            DialogUtils.error(e.getMessage());
        }
    }
}
