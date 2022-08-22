package com.github.superzhc.hadoop.hdfs.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.TableViewUtils;
import com.github.superzhc.hadoop.hdfs.HdfsRestApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/8/22 15:53
 **/
public class ToolHdfsController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private TextField txtHost;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPath;

    @FXML
    private TableView<Map<String, String>> tv;

    private FileChooser fileChooser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:\\downloads"));
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    @FXML
    public void btnGoAction(ActionEvent actionEvent) {
        String host = txtHost.getText();
        if (null == host || host.trim().length() == 0) {
            DialogUtils.error("请输入NameNode Host");
            return;
        }

        String port = txtPort.getText();
        if (null == port || port.trim().length() == 0) {
            DialogUtils.error("请输入NameNode Port");
            return;
        }

        Integer iPort;
        try {
            iPort = Integer.valueOf(port);
        } catch (Exception e) {
            DialogUtils.error("请输入有效NameNode Port", e);
            return;
        }

        String user = txtUser.getText();
        HdfsRestApi api = new HdfsRestApi(host, iPort, (null == user || user.trim().length() == 0) ? null : user);

        String path = txtPath.getText();
        if (null == path || path.trim().length() == 0) {
            path = "/";
        }

        try {
            String result = api.list(path);
            JsonNode fileStatus = JsonUtils.json(result, "FileStatuses", "FileStatus");
            Map<String, String>[] maps = JsonUtils.objectArray2Map(fileStatus);
            TableViewUtils.bind(tv, maps);
        } catch (Exception e) {
            DialogUtils.error("Browse Directory Error", e);
        }
    }

    @FXML
    public void btnUploadAction(ActionEvent actionEvent) {
        String host = txtHost.getText();
        if (null == host || host.trim().length() == 0) {
            DialogUtils.error("请输入NameNode Host");
            return;
        }

        String port = txtPort.getText();
        if (null == port || port.trim().length() == 0) {
            DialogUtils.error("请输入NameNode Port");
            return;
        }

        Integer iPort;
        try {
            iPort = Integer.valueOf(port);
        } catch (Exception e) {
            DialogUtils.error("请输入有效NameNode Port", e);
            return;
        }

        String user = txtUser.getText();
        File file = fileChooser.showOpenDialog(getStage());
        if (null != file) {
            HdfsRestApi api = new HdfsRestApi(host, iPort, (null == user || user.trim().length() == 0) ? null : user);

            String path = txtPath.getText();
            if (null == path || path.trim().length() == 0) {
                path = "/";
            }

            try {
                api.upload(path, file);
            } catch (Exception e) {
                DialogUtils.error("Browse Directory Error", e);
            }
        }
    }


}
