package com.github.superzhc.hadoop.kafka.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2022/7/27 16:51
 **/
public class ToolKafkaController {
    public static final String FXML_PATH = "tool_kafka.fxml";

    @FXML
    private TextField txtBrokers;

    @FXML
    private ComboBox<String> cbTopics;

    /*消费者*/

    /*生产者*/
    @FXML
    private TextArea txtMessage;

    @FXML
    private TextArea txtOutput;

    @FXML
    public void btnInitCbTopicsAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            Set<String> topics = adminClient.list();
            cbTopics.setItems(FXCollections.observableList(new ArrayList<>(topics)));
            DialogUtils.info("消息", "获取成功");
        }
    }

    @FXML
    public void btnGetALLGroupAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            List<String> groups = adminClient.consumerGroups();
            StringBuilder sb = new StringBuilder();
            sb.append("获取当前Kafka集群的所有消费者组：\n");
            sb.append(groups.stream().collect(Collectors.joining("\n")));
            txtOutput.setText(sb.toString());
        }
    }

    @FXML
    public void btnGetTopicGroupAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = cbTopics.getValue();
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("消息", "请输入主题");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            List<String> groups = adminClient.consumerGroups(topic);
            StringBuilder sb = new StringBuilder();
            sb.append("获取主题【" + topic + "】的所有消费者组：\n");
            sb.append(groups.stream().collect(Collectors.joining("\n")));
            txtOutput.setText(sb.toString());
        }
    }

    @FXML
    public void btnExistTopicAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = cbTopics.getValue();
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("消息", "请输入主题");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            boolean b = adminClient.exist(topic);
            DialogUtils.info("消息", "主题【" + topic + "】" + (b ? "存在" : "不存在"));
        }
    }

    @FXML
    public void btnSearchTopics(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = cbTopics.getValue();
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("消息", "请输入主题");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            Set<String> topics = adminClient.list();

            List<String> matchTopics = new ArrayList<>();
            for (String str : topics) {
                if (str.contains(topic)) {
                    matchTopics.add(str);
                }
            }

            txtOutput.setText(matchTopics.stream().collect(Collectors.joining("\n")));
        }
    }
}
