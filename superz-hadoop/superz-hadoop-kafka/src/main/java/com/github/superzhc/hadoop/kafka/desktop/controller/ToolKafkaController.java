package com.github.superzhc.hadoop.kafka.desktop.controller;

import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyProducer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2022/7/27 16:51
 **/
public class ToolKafkaController {
    private static final Logger log = LoggerFactory.getLogger(ToolKafkaController.class);

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

    /**
     * 连接 Kafka Broker，获取所有的主题
     *
     * @param actionEvent
     */
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

    /**
     * 获取 Kafka 集群的所有消费者组
     *
     * @param actionEvent
     */
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

    @FXML
    public void btnProducerSendAction(ActionEvent actionEvent) {
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

        String message = txtMessage.getText();
        if (null == message || message.trim().length() == 0) {
            DialogUtils.error("消息", "请输入消息");
            return;
        }

        try (MyProducer producer = new MyProducer(brokers)) {
            RecordMetadata recordMetadata = producer.send(topic, null, message);
            String msg = String.format("发送消息到主题【%s】的第【%d】分区，偏移量为【%d】，时间戳为【%d】", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
            DialogUtils.info("消息", msg);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("发送消息异常", e);
            DialogUtils.error("错误", e.getMessage());
            return;
        }
    }
}
