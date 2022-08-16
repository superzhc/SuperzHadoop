package com.github.superzhc.hadoop.kafka.desktop.controller;

import com.github.superzhc.common.faker.utils.ExpressionUtils;
import com.github.superzhc.common.format.PlaceholderResolver;
import com.github.superzhc.common.javafx.DialogUtils;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyProducer;
import com.github.superzhc.hadoop.kafka.util.ConsumerRecordUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2022/7/27 16:51
 **/
public class ToolKafkaController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(ToolKafkaController.class);

    public static final String FXML_PATH = "tool_kafka.fxml";

    @FXML
    private TextField txtBrokers;

    @FXML
    private ComboBox<String> cbTopics;

    /*消费者*/
    @FXML
    private TextField txtGroupId;

    /*生产者*/
    @FXML
    private TextArea txtMessage;

    @FXML
    private TextArea txtOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtMessage.setText("{\"name\":\"#{Name.name}\",\"age\":#{number.number_between '1','80'}}");
    }

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
            if (null == topics) {
                DialogUtils.error("无主题");
                return;
            } else {
                cbTopics.setItems(FXCollections.observableList(new ArrayList<>(topics)));
                txtOutput.setText(topics.stream().collect(Collectors.joining("\n")));
                DialogUtils.info("消息", "获取成功");
            }
        }
    }

    @FXML
    public void btnCreateTopicAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = DialogUtils.prompt("请输入主题名称");
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("请输入主题名称");
            return;
        }

        try (MyAdminClient adminClient = new MyAdminClient(brokers)) {
            adminClient.create(topic, 3, (short) 1, null);
            DialogUtils.info("主题创建成功");
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
    public void btnTopicDescribe(ActionEvent actionEvent) {
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
            TopicDescription description = adminClient.describe(topic);
            List<TopicPartitionInfo> partitions = description.partitions();

            StringBuilder sb = new StringBuilder();
            sb.append("主题名称：").append(description.name()).append("\n");
            sb.append("分区数：").append(partitions.size()).append("\n");

            txtOutput.setText(sb.toString());
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
            DialogUtils.error("消息", "请选择主题");
            return;
        }

        String message = txtMessage.getText();
        if (null == message || message.trim().length() == 0) {
            DialogUtils.error("消息", "请输入消息");
            return;
        }

        try (MyProducer producer = new MyProducer(brokers)) {
            String kafkaMsg = ExpressionUtils.convert(message);
            RecordMetadata recordMetadata = producer.send(topic, null, kafkaMsg);
            String msg = String.format("发送消息【%s】到主题【%s】的第【%d】分区，偏移量为【%d】，时间戳为【%d】", kafkaMsg, recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
            DialogUtils.info("消息", msg);
        } catch (Exception e) {
            log.error("发送消息异常", e);
            DialogUtils.error("错误", e.getMessage());
            return;
        }
    }

    @FXML
    public void btnProducerBatchSendAction(ActionEvent actionEvent) {
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

        String strNum = DialogUtils.prompt("信息", "请输入批量发送的条数", "100");
        if (null == strNum) {
            return;
        }

        if (strNum.trim().length() == 0) {
            DialogUtils.error("消息", "请输入批量发送的信息条数");
            return;
        }

        Integer num = null;
        try {
            num = Integer.parseInt(strNum);
        } catch (Exception e) {
            DialogUtils.error("消息", "请输入有效批量发送的信息条数");
            return;
        }

        // 提取 java faker 的 expression
        List<String> expressions = ExpressionUtils.extractExpressions(message, false);
        PlaceholderResolver resolver = PlaceholderResolver.getResolver("#{", "}");

        try (MyProducer producer = new MyProducer(brokers)) {
            txtOutput.setText(null);
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < num; i++) {
                Map<String, String> map = ExpressionUtils.build(expressions, false);
                String kafkaMsg = resolver.resolveByMap(message, map);
                RecordMetadata recordMetadata = producer.send(topic, null, kafkaMsg);
                String msg = String.format("发送消息【%s】到主题【%s】的第【%d】分区，偏移量为【%d】，时间戳为【%d】", kafkaMsg, recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                output.append(msg).append("\n");
                txtOutput.setText(output.toString());
            }
            DialogUtils.info("消息", "批量发送成功");
        } catch (Exception e) {
            log.error("发送消息异常", e);
            DialogUtils.error("错误", e.getMessage());
            return;
        }
    }

    @FXML
    public void btnConsumerAction(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = cbTopics.getValue();
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("消息", "请选择主题");
            return;
        }

        String groupId = txtGroupId.getText();
        if (null == groupId || groupId.trim().length() == 0) {
            DialogUtils.error("消息", "请输入消费者组");
            return;
        }

        txtOutput.setText(consumer(brokers, groupId, topic, 10));
    }

    @FXML
    public void btnConsumerAction2(ActionEvent actionEvent) {
        String brokers = txtBrokers.getText();
        if (null == brokers || brokers.trim().length() == 0) {
            DialogUtils.error("消息", "请输入Brokers");
            return;
        }

        String topic = cbTopics.getValue();
        if (null == topic || topic.trim().length() == 0) {
            DialogUtils.error("消息", "请选择主题");
            return;
        }

        String groupId = txtGroupId.getText();
        if (null == groupId || groupId.trim().length() == 0) {
            DialogUtils.error("消息", "请输入消费者组");
            return;
        }

        String numStr = DialogUtils.prompt("请输入消费条数", "10");
        int num = 10;
        try {
            num = Integer.valueOf(numStr);
        } catch (Exception ex) {
            DialogUtils.error(ex);
            return;
        }

        txtOutput.setText(consumer(brokers, groupId, topic, num));
    }

    private String consumer(String brokers, String groupId, String topic, Integer num) {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        // 订阅主题
        consumer.subscribe(Collections.singleton(topic));

        //获取分配的分区信息
        Set<TopicPartition> assignment = consumer.assignment();
        int counter = 3;
        while (assignment.isEmpty() && counter > 0) {
            consumer.poll(Duration.ofSeconds(1));
            assignment = consumer.assignment();
        }

        StringBuilder sb = new StringBuilder();
        //获取每个分区的最新偏移量
        Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(assignment);
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(assignment);
        for (TopicPartition topicPartition : assignment) {
            Long beginOffset = beginOffsets.get(topicPartition);
            Long endOffset = endOffsets.get(topicPartition);
            // endOffset为0，说明此分区没有消息，跳过循环
            if (endOffset == 0L) {
                continue;
            }

            // 指定消费分区最后一条消息
            consumer.seek(topicPartition, endOffset - beginOffset > num ? (endOffset - num) : beginOffset);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            int j = 0;
            while (records.isEmpty() && j++ < 3) {
                records = consumer.poll(Duration.ofMillis(1000));
            }
            for (ConsumerRecord<String, String> record : records) {
                sb.append(ConsumerRecordUtils.human(record)).append("\n");
            }
        }

        // 解除订阅
        consumer.unsubscribe();
        consumer.close();

        return sb.toString();
    }
}
