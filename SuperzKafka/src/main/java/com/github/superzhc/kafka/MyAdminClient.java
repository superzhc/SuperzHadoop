package com.github.superzhc.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 2020年04月26日 superz add
 */
public class MyAdminClient extends KafkaBrokers implements Closeable
{
    private long timeout = 30;
    private AdminClient adminClient;

    public MyAdminClient(String brokers) {
        this(brokers, null);
    }

    public MyAdminClient(String brokers, Map<String, String> properties) {
        super(brokers);

        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
        if (null != properties) {
            for (Map.Entry<String, String> property : properties.entrySet()) {
                props.put(property.getKey(), property.getValue());
            }
        }
        adminClient = AdminClient.create(props);
    }

    public void create(String topicName, int partitions, short replication, Map<String, String> configs) {
        try {
            NewTopic topic = new NewTopic(topicName, partitions, replication);
            if (null != configs) {
                topic.configs(configs);
            }
            CreateTopicsResult result = adminClient.createTopics(Collections.singleton(topic));
            result.all().get(timeout, TimeUnit.SECONDS);
        }
        catch (Exception e) {
        }
    }

    public void delete(String topic) {
        // 服务端server.properties需要设置delete.topic.enable=true，才可以使用同步删除，否则只是将主题标记为删除
        DeleteTopicsResult result = adminClient.deleteTopics(Arrays.asList(topic));
        // 同步操作出现超时情况，见上配置是否配置了
        // note 为了更好的兼容性，此处不提供同步获取删除结果
        // result.all().get();
    }

    /**
     * 获取所有的主题
     * @return
     */
    public Set<String> list() {
        try {
            ListTopicsResult result = adminClient.listTopics();
            Set<String> topics = result.names().get(1, TimeUnit.SECONDS);
            return topics;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断主题是否存在
     * @param topic 主题名
     * @return
     * @throws Exception
     */
    public boolean exist(String topic) {
        Set<String> topics = list();
        return topics.contains(topic);
    }

    /**
     * 获取主题的描述信息
     * @param topic 主题名
     * @return
     * @throws Exception
     */
    public TopicDescription describe(String topic) {
        try {
            TopicDescription description = adminClient.describeTopics(Arrays.asList(topic)).all()
                    .get(timeout, TimeUnit.SECONDS).get(topic);
            return description;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 主题下的所有分区
     * @param topic
     * @return
     * @throws Exception
     */
    public List<TopicPartition> topicPartitions(String topic) {
        List<TopicPartitionInfo> partitionInfos = describe(topic).partitions();
        List<TopicPartition> result = new ArrayList<>();
        for (TopicPartitionInfo partitionInfo : partitionInfos) {
            result.add(new TopicPartition(topic, partitionInfo.partition()));
        }
        return result;
    }

    /**
     * 添加分区数
     * @param topic 主题名
     * @param numPartitions 原有的分区数+新增分区数量
     */
    public void addPartitions(String topic, Integer numPartitions) throws Exception {
        NewPartitions newPartitions = NewPartitions.increaseTo(numPartitions);
        Map<String, NewPartitions> map = new HashMap<>(1, 1);
        map.put(topic, newPartitions);
        adminClient.createPartitions(map).all().get(timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取Kafka集群中的所有消费者组
     * @return
     * @throws Exception
     */
    public List<String> consumerGroups() {
        try {
            List<String> allGroups = adminClient.listConsumerGroups() //
                    .valid()//
                    .get(timeout, TimeUnit.SECONDS)//
                    .stream()//
                    .map(ConsumerGroupListing::groupId)//
                    .collect(Collectors.toList());

            return allGroups;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取主题下的所有消费者组
     * @param topic
     * @return
     * @throws Exception
     */
    public List<String> consumerGroups(String topic) {
        try {
            List<String> allGroups = consumerGroups();

            Map<String, ConsumerGroupDescription> allGroupDetails = adminClient.describeConsumerGroups(allGroups).all()
                    .get(timeout, TimeUnit.SECONDS);
            List<String> filteredGroups = new ArrayList<>();
            allGroupDetails.entrySet().forEach(entry -> {
                String groupId = entry.getKey();
                ConsumerGroupDescription description = entry.getValue();
                boolean topicSubscribed = description.members().stream().map(MemberDescription::assignment)
                        .map(MemberAssignment::topicPartitions)
                        .map(topics -> topics.stream().map(TopicPartition::topic).collect(Collectors.toSet()))
                        .anyMatch(topics -> topics.contains(topic));
                if (topicSubscribed)
                    filteredGroups.add(groupId);
            });
            return filteredGroups;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (null != adminClient)
            adminClient.close();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }
}
