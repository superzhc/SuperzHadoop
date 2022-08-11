package com.github.superzhc.common.javafx.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.common.utils.ProcessUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Java 命令行工具
 *
 * @author superz
 * @create 2022/8/11 10:13
 **/
public class ToolJavaCLIController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(ToolJavaCLIController.class);

    public static final String FXML_PATH = "../view/tool_java_cli.fxml";

    public static URL getFxmlPath() {
        return ToolJavaCLIController.class.getResource(FXML_PATH);
    }

    private Stage stage;

    @FXML
    private VBox root;

    private FileChooser fileChooser = null;

    @FXML
    private TextField txtJarPath;

    /**
     * jar 包是否定义 main class
     */
    @FXML
    private CheckBox cbIsDefineMainClass;

    @FXML
    private TextField txtMainClass;

    @FXML
    private TextArea txtProperties;

    @FXML
    private TextArea txtMainArgs;

    @FXML
    private TextArea txtDependencies;

    @FXML
    private TextArea txtOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:\\"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JAR", "*.jar"));

        cbIsDefineMainClass.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                txtMainClass.setVisible(!newValue);
            }
        });
    }

    public Stage getStage() {
        if (null == stage) {
            stage = (Stage) root.getScene().getWindow();
        }
        return stage;
    }

    @FXML
    public void btnChooseFile(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(getStage());
        if (null != file) {
            txtJarPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void btnGetDependencies(ActionEvent actionEvent) {
        StringBuilder sb = new StringBuilder();
        List<File> files = fileChooser.showOpenMultipleDialog(getStage());
        for (File file : files) {
            sb.append(";").append(file.getAbsolutePath());
        }
        txtDependencies.setText(sb.substring(1));
    }

    @FXML
    public void btnGenerateCLI(ActionEvent actionEvent) {
        String jarPath = txtJarPath.getText();
        if (null == jarPath || jarPath.trim().length() == 0) {
            DialogUtils.error("请指定JAR包");
            return;
        }

        boolean isDefineMainClass = cbIsDefineMainClass.isSelected();
        String mainClass = null;
        if (!isDefineMainClass) {
            mainClass = txtMainClass.getText();
            if (null == mainClass || mainClass.trim().length() == 0) {
                DialogUtils.error("JAR包未指定Main函数，请手动填写Main函数");
                return;
            }
        }

        String properties = txtProperties.getText();

        String mainArgs = txtMainArgs.getText();

        String dependencies = txtDependencies.getText();

        List<String> commands = generateCLI(jarPath, properties, dependencies, mainClass, mainArgs);
        String cli = commands.stream().collect(Collectors.joining(" "));
        txtOutput.setText(cli);
    }

    @FXML
    public void btnRunCLI(ActionEvent actionEvent) {
        String jarPath = txtJarPath.getText();
        if (null == jarPath || jarPath.trim().length() == 0) {
            DialogUtils.error("请指定JAR包");
            return;
        }

        boolean isDefineMainClass = cbIsDefineMainClass.isSelected();
        String mainClass = null;
        if (!isDefineMainClass) {
            mainClass = txtMainClass.getText();
            if (null == mainClass || mainClass.trim().length() == 0) {
                DialogUtils.error("JAR包未指定Main函数，请手动填写Main函数");
                return;
            }
        }

        String properties = txtProperties.getText();
        String mainArgs = txtMainArgs.getText();
        String dependencies = txtDependencies.getText();

        txtOutput.setText("");

        List<String> commands = generateCLI(jarPath, properties, dependencies, mainClass, mainArgs);
        ProcessUtils.exec(commands, new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) {
                String encoding = System.getProperty("sun.jnu.encoding");
                try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
                    String line;
                    while ((line = output.readLine()) != null) {
                        StringBuilder sb = new StringBuilder(txtOutput.getText());
                        sb.append(line).append("\n");
                        txtOutput.setText(sb.toString());
                    }
                } catch (Exception e) {
                    txtOutput.setText(e.toString());
                }
            }
        });
    }

    private List<String> generateCLI(String jarPackage, String sysProperties, String dependencies, String mainClass, String mainArgs) {
        List<String> commands = new ArrayList<>();
        commands.add("java");

        /*系统参数处理，形如：-Dk1=v1*/
        if (null != sysProperties && sysProperties.trim().length() > 0) {
            commands.addAll(resolverConfigString(sysProperties));
        }

        /*依赖处理*/
        // 当前只考虑了windows系统，使用分号分隔符
        String seperator = ";";
        StringBuilder cp = new StringBuilder();
        if (null != mainClass && mainClass.trim().length() > 0) {
            cp.append(seperator).append(jarPackage);
        }
        if (null != dependencies && dependencies.trim().length() > 0) {
            cp.append(seperator).append(dependencies);
        }
        if (cp.length() > 0) {
            commands.add("-cp");
            commands.add(cp.substring(1));
        }

        /*主函数*/
        if (null != mainClass && mainClass.trim().length() > 0) {
            commands.add(mainClass);
        } else {
            commands.add("-jar");
            commands.add(jarPackage);
        }

        /*参数处理*/
        if (null != mainArgs && mainArgs.trim().length() > 0) {
            commands.addAll(resolverConfigString(mainArgs));
        }

        return commands;
    }

    private List<String> resolverConfigString(String str) {
        List<String> configs = new ArrayList<>();
        str = str.trim();
        int length = str.length();
        int cursor = 0;
        int start = 0;
        boolean isQuote = false;
        while (cursor < length) {
            char c = str.charAt(cursor);
            if (c == '"') {
                isQuote = !isQuote;
            } else if (c == ' ' && !isQuote) {// 如果双引号内的空格，这个是转义的
                String config = str.substring(start, cursor);
                configs.add(config);
                start = cursor + 1;
            }

            cursor++;
        }

        if (start < length) {
            configs.add(str.substring(start));
        }

        log.debug("参数字符串解析：{}", configs);
        return configs;
    }
}
