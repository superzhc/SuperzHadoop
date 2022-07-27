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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;
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
    private CheckBox ckbHeader;

    @FXML
    private TextField txtSeparator;

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
                , new FileChooser.ExtensionFilter("JSON", "*.json")
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
        final Integer lineNums;
        String nums = DialogUtils.prompt("消息", "请输入预览行数", "100");
        if (null == nums || nums.trim().length() == 0) {
            DialogUtils.error("错误", "请输入预览行数");
            return;
        } else {
            try {
                lineNums = Integer.valueOf(nums);
            } catch (Exception e) {
                DialogUtils.error("错误", e.getMessage());
                return;
            }
        }

        String path = txtFilePath.getText();
        if (null == path || path.trim().length() == 0) {
            DialogUtils.error("消息", "请输入文件地址");
            return;
        }

        boolean isHeader = ckbHeader.isSelected();
        String separator = txtSeparator.getText();
        boolean isSeparator = !(null == separator || separator.trim().length() == 0);

        // 预览只执行一次
        TextReader.TextReaderSetting setting = new TextReader.TextReaderSetting();
        setting.setPath(path);
        setting.setBatchSize(lineNums > 1000 ? 1000 : lineNums);
        setting.setCharset(cbCharset.getValue());

        List<Map<String, String>> data = new ArrayList<>();
        setting.setDataFunction(new Function<List<String>, Object>() {
            int counter = 0;

            List<String> columns = new ArrayList<>();

            @Override
            public Object apply(List<String> strings) {
                int header = 0;
                for (String str : strings) {
                    if (isHeader && header == 0) {
                        if (!isSeparator) {
                            columns.add(str);
                        } else {
                            columns.addAll(Arrays.asList(str.split(separator, -1)));
                        }
                        header++;
                    } else {
                        if (!isSeparator) {
                            if (columns.size() == 0) {
                                columns.add("Content");
                            }
                            Map<String, String> row = new HashMap<>();
                            row.put("Content", str);
                            data.add(row);
                        } else {
                            String[] arr = str.split(separator, -1);
                            if (columns.size() == 0) {
                                for (int i = 0, len = arr.length; i < len; i++) {
                                    columns.add(String.format("c%d", (i + 1)));
                                }
                            }
                            Map<String, String> row = new LinkedHashMap<>();
                            for (int i = 0, len = arr.length; i < len; i++) {
                                String value;
                                if (i > columns.size() - 1) {
                                    value = (String) row.get(columns.get(columns.size() - 1));
                                    value += separator + arr[i];
                                    row.put(columns.get(columns.size() - 1), arr[i]);
                                } else {
                                    value = arr[i];
                                    row.put(columns.get(i), value);
                                }
                            }
                            data.add(row);
                        }
                        counter++;
                    }
                }
                return counter < lineNums;
            }
        });
        TextReader.read(setting);

        table.getColumns().clear();

        table.getColumns().add(TableViewUtils.numbers("行号"));

        table.getColumns().addAll(TableViewUtils.bind(data));

        table.setItems(FXCollections.observableList(data));
    }
}
