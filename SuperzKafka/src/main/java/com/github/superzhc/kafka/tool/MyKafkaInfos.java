package com.github.superzhc.kafka.tool;

import com.github.superzhc.kafka.MyAdminClient;
import com.github.superzhc.kafka.util.FormatUtils;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MyKafkaInfos {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaInfos.class);

    private static final String pattern = "^(V2_Up_)([A-Za-z0-9]+)(_Event_ObjectPTC)$";//指定主题信息打印

    public static void main(String[] args) {
        final String brokers = MyKafkaConfigs.JSDZ_BROKER;

        try (MyAdminClient myAdminClient = new MyAdminClient(brokers)) {
            // 获取所有主题
            Set<String> topics = myAdminClient.list();
//            log.info("主题信息：\n\t主题数：{}\n\t主题列表：\n\t\t{}", topics.size(), topics.stream().collect(Collectors.joining("\n\t\t")));
            StringBuilder topicDetails = new StringBuilder();
            int count = 0;
            for (String topic : topics) {
                if ((null != pattern && pattern.length() > 0) && !Pattern.matches(pattern, topic)) {
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


}
