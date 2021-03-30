package com.github.superzhc.kafka.tool;

import com.github.superzhc.kafka.MyAdminClient;
import com.github.superzhc.kafka.util.FormatUtils;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author superz
 */
public abstract class MyKafkaInfos extends MyBasicTool {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaInfos.class);

    public void run(String[] args) {
        final String brokers = brokers();

        try (MyAdminClient myAdminClient = new MyAdminClient(brokers)) {
            // 获取所有主题
            Set<String> topics = myAdminClient.list();
            StringBuilder topicDetails = new StringBuilder();
            int count = 0;
            for (String topic : topics) {
                if ((null != patternTopic() && patternTopic().length() > 0) && !Pattern.matches(patternTopic(), topic)) {
                    continue;
                }

                count++;
                topicDetails.append(FormatUtils.multiLevelTab(2)).append(topic).append("\n");

//                // 主题描述
//                TopicDescription description = myAdminClient.describe(topic);
//                topicDetails.append(FormatUtils.multiLevelTab(3)).append(description).append("\n");

                // 分区信息
                List<TopicPartition> partitions = myAdminClient.topicPartitions(topic);
                topicDetails.append(FormatUtils.multiLevelTab(3)).append("分区列表：").append(partitions).append("\n");
            }

            log.info("主题信息：\n{}主题数：{}\n{}主题列表：\n{}", FormatUtils.multiLevelTab(1), count, FormatUtils.multiLevelTab(1), topicDetails.toString());
        }
    }

    protected String patternTopic() {
        return null;
    }
}
