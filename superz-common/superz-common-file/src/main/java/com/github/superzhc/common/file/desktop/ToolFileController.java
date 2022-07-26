package com.github.superzhc.common.file.desktop;

import com.github.superzhc.common.file.text.TextReader;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.javafx.TableViewUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/7/26 15:21
 **/
public class ToolFileController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(ToolFileController.class);

    @FXML
    private VBox root;

    @FXML
    private TextField txtFilePath;

    @FXML
    private ComboBox<String> cbCharset;

    @FXML
    private TableView table;

    private Stage stage;
    private FileChooser chooser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCharset.getItems().addAll("UTF-8", "GB2312", "GBK");
        cbCharset.setValue("UTF-8");

        chooser = new FileChooser();
        chooser.setTitle("请选择文件");
        // 初始化目录
        chooser.setInitialDirectory(new File("D://"));
        // 设置可预览的文件后缀
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文本文件", "*.txt")
                , new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }

    public Stage getStage() {
        if (null == stage) {
            stage = (Stage) root.getScene().getWindow();
        }
        return stage;
    }

    @FXML
    public void btnChoose(ActionEvent actionEvent) {
        File file = chooser.showOpenDialog(getStage());
        if (null != file) {
            txtFilePath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void btnPreview(ActionEvent actionEvent) {
        String path = txtFilePath.getText();
        if (null == path || path.trim().length() == 0) {
            DialogUtils.error("消息", "请输入文件地址");
        }

        table.getColumns().clear();
        TableColumn lineColumn = TableViewUtils.numbers("行号");
        table.getColumns().add(lineColumn);

        TableColumn contentColumn = new TableColumn("Content");
        contentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row = (String) param.getValue();
                return new SimpleStringProperty(row);
            }
        });
        table.getColumns().add(contentColumn);

        // 预览只执行一次
        TextReader.TextReaderSetting setting = new TextReader.TextReaderSetting();
        setting.setPath(path);
        setting.setBatchSize(100);
        setting.setCharset(cbCharset.getValue());
        setting.setDataFunction(new Function<List<String>, Object>() {
            @Override
            public Object apply(List<String> strings) {
                table.setItems(FXCollections.observableList(strings));
                return false;
            }
        });
        TextReader.read(setting);
    }
}
