package com.github.superzhc.hadoop.es.desktop.controller;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.document.ESDocument;
import com.github.superzhc.hadoop.es.index.ESIndex;
import com.github.superzhc.hadoop.es.search.ESNewSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author superz
 * @create 2022/7/27 10:22
 **/
public class ToolESController implements Initializable {
    public static final String FXML_PATH = "tool_es.fxml";

    @FXML
    private TextField txtHost;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnDisconnect;

    @FXML
    private ComboBox<String> cbIndices;

    @FXML
    private HBox ccbContainer;
    private CheckComboBox<String> ccbIndices;

    @FXML
    private TextArea txtRequest;

    @FXML
    private TextArea txtResponse;

    private ESClient client = null;
    private boolean isConnect = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ccbIndices = new CheckComboBox<>();
        ccbIndices.setPrefHeight(25);
        ccbIndices.setPrefWidth(120);
        ccbContainer.getChildren().add(ccbIndices);

        txtHost.setText("10.90.15.142");
        txtUsername.setText("elastic");
        txtPassword.setText("xgxx@elastic");
    }

    /**
     * 连接Elasticsearch
     *
     * @param actionEvent
     */
    @FXML
    public void btnConnectAction(ActionEvent actionEvent) {
        String host = txtHost.getText();
        if (null == host || host.trim().length() == 0) {
            DialogUtils.error("消息", "请输入 Host");
            return;
        }

        String strPort = txtPort.getText();
        if (null == strPort || strPort.trim().length() == 0) {
            DialogUtils.error("消息", "请输入 Port");
            return;
        }

        Integer port = null;
        try {
            port = Integer.valueOf(strPort);
        } catch (Exception e) {
            DialogUtils.error("消息", "请输入有效 Port");
            return;
        }

        String username = txtUsername.getText();
        if (null != username && username.trim().length() == 0) {
            username = null;
        }
        String password = txtPassword.getText();

        client = ESClient.create(host, port, username, password);
        isConnect = true;
        txtHost.setDisable(true);
        txtPort.setDisable(true);
        txtUsername.setDisable(true);
        txtPassword.setDisable(true);
        btnConnect.setDisable(true);
        btnDisconnect.setDisable(false);

        // 初始化索引
        ESIndex indexClient = new ESIndex(client);
        List<String> indices = indexClient.indices();

        cbIndices.setItems(FXCollections.observableList(indices));
        ccbIndices.getItems().addAll(indices);
    }

    /**
     * 断开连接
     *
     * @param actionEvent
     */
    @FXML
    public void btnDisconnectAction(ActionEvent actionEvent) {
        try {
            client.close();
            isConnect = false;
            txtHost.setDisable(false);
            txtPort.setDisable(false);
            txtUsername.setDisable(false);
            txtPassword.setDisable(false);
            btnConnect.setDisable(false);
            btnDisconnect.setDisable(true);

            cbIndices.setItems(null);
            ccbIndices.setTitle(null);
            ccbIndices.getItems().clear();
        } catch (Exception e) {
            DialogUtils.error("消息", e.getMessage());
        }
    }

    @FXML
    public void btnGetIndexInfoAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("消息", "请先连接Elasticsearch");
            return;
        }

        String index = cbIndices.getValue();
        if (null == index || index.trim().length() == 0) {
            DialogUtils.error("消息", "请选择索引");
            return;
        }

        txtResponse.setText(null);

        ESIndex indexClient = new ESIndex(client);
        String indexInfo = indexClient.get(index);
        txtResponse.setText(JsonUtils.format(indexInfo));
    }

    @FXML
    public void btnGetIndexMappingsAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("消息", "请先连接Elasticsearch");
            return;
        }

        String index = cbIndices.getValue();
        if (null == index || index.trim().length() == 0) {
            DialogUtils.error("消息", "请选择索引");
            return;
        }

        txtResponse.setText(null);

        ESIndex indexClient = new ESIndex(client);
        String indexMappings = indexClient.mapping(index);
        txtResponse.setText(JsonUtils.format(indexMappings));
    }

    @FXML
    public void btnAddDocumentAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("消息", "请先连接Elasticsearch");
            return;
        }

        String index = cbIndices.getValue();
        if (null == index || index.trim().length() == 0) {
            DialogUtils.error("消息", "请选择索引");
            return;
        }

        String data = txtRequest.getText();
        if (null == data || data.trim().length() == 0) {
            DialogUtils.error("消息", "请输入请求数据");
            return;
        }

        ESDocument docClient = new ESDocument(client);
        String result = docClient.add(index, data);
        txtResponse.setText(JsonUtils.format(result));
    }

    @FXML
    public void btnSearchAllAction(ActionEvent actionEvent) {
        if (!isConnect) {
            DialogUtils.error("消息", "请先连接Elasticsearch");
            return;
        }

        ESNewSearch search = new ESNewSearch(client);

        String[] indices = null;

        ObservableList<String> checkedItems = ccbIndices.getCheckModel().getCheckedItems();
        if (null != checkedItems && checkedItems.size() > 0) {
            indices = new String[checkedItems.size()];
            for (int i = 0, len = checkedItems.size(); i < len; i++) {
                indices[i] = checkedItems.get(i);
            }
        }
        String result = search.queryAll(indices);
        txtResponse.setText(JsonUtils.format(result));
    }
}
