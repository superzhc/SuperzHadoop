package com.github.superzhc.hadoop.kafka;

import kafka.admin.AdminClient;
import kafka.admin.AdminUtils;
import kafka.api.TopicMetadata;
import kafka.cluster.Broker;
import kafka.coordinator.GroupOverview;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * kafka 0.9.1.0 不支持 KafkaAdminClient，通过服务端 kafka_2.11.jar 包来管理集群
 *
 * @author superz
 * @create 2022/11/9 17:05
 **/
public class MyAdminClient {
    private static final Logger log = LoggerFactory.getLogger(MyAdminClient.class);

    private String zookeeper;

    public MyAdminClient(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public List<String> list() {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 3000, 3000, JaasUtils.isZkSecurityEnabled());
            List<String> topics = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
            return topics;
        } finally {
            if (null != zkUtils)
                zkUtils.close();
        }
    }

    public List<Integer> getPartitionsForTopic(String topic) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 3000, 3000, JaasUtils.isZkSecurityEnabled());

            List<Object> partitions = JavaConversions.seqAsJavaList(
                    JavaConversions.mapAsJavaMap(
                            zkUtils.getPartitionsForTopics(JavaConverters.asScalaIteratorConverter(Arrays.asList(topic).iterator()).asScala().toSeq())
                    ).get(topic)
            );

            return partitions.stream().map(d -> (int) d).collect(Collectors.toList());
        } finally {
            if (null != zkUtils)
                zkUtils.close();
        }
    }

//    public Set<String> getAllConsumerGroupsForTopic(String topic) {
//        ZkUtils zkUtils = null;
//        try {
//            zkUtils = ZkUtils.apply(zookeeper, 3000, 3000, JaasUtils.isZkSecurityEnabled());
//            // 0.9.1.0版本的kafka的消费者组和偏移量不在zookeeper中了
//            //Set<String> groups = JavaConversions.setAsJavaSet(zkUtils.getAllConsumerGroupsForTopic(topic));
//            AdminClient adminClient=adminClient(zkUtils);
//
//            Map<Node, scala.collection.immutable.List<GroupOverview>> groups=JavaConversions.mapAsJavaMap(adminClient.listAllGroups());
//            for(Map.Entry<Node, scala.collection.immutable.List<GroupOverview>> group:groups.entrySet()){
//                List<GroupOverview> groupOverviews= JavaConversions.seqAsJavaList(group.getValue());
//
//            }
//
//            return null;
//        } finally {
//            if (null != zkUtils)
//                zkUtils.close();
//        }
//    }

//    public void getTopicParitionOffset(String topic, int partition) {
//        ZkUtils zkUtils = null;
//        try {
//            zkUtils = ZkUtils.apply(zookeeper, 3000, 3000, JaasUtils.isZkSecurityEnabled());
//
//            TopicMetadata metadata = AdminUtils.fetchTopicMetadataFromZk(topic, zkUtils);
//
//            System.out.println(metadata.toString());
//        } finally {
//            if (null != zkUtils)
//                zkUtils.close();
//        }
//    }

    public Long earliestOffset(String topic, int partition) {
        Long offset = earliestOffset(topic).get(partition);
        return offset;
    }

    public Map<Integer, Long> earliestOffset(String topic) {
        return offset(topic, "earliest");
    }

    public Long latestOffset(String topic, int partition) {
        Long offset = latestOffset(topic).get(partition);
        return offset;
    }

    public Map<Integer, Long> latestOffset(String topic) {
        return offset(topic, "latest");
    }

    private Map<Integer, Long> offset(String topic, String type) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 3000, 3000, JaasUtils.isZkSecurityEnabled());
            String brokers = brokers(zkUtils);

            Properties properties = new Properties();
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
            // 这个是唯一值，创建新的消费者组
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, String.format("superz-%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            // 固定值，要获取最新的偏移量
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, type/*"latest"*/);
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
                consumer.subscribe(Collections.singletonList(topic));
                // 需要先订阅分配主题
                consumer.poll(100);

                List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
                Map<Integer, Long> offsets = new LinkedHashMap<>();
                for (PartitionInfo partitionInfo : partitionInfos) {
                    long offset = consumer.position(new TopicPartition(topic, partitionInfo.partition()));
                    offsets.put(partitionInfo.partition(), offset);
                }

                log.debug("主题【{}】的{}偏移量：{}", topic, type, offsets);
                return offsets;
            }
        } finally {
            if (null != zkUtils)
                zkUtils.close();
        }
    }

    private String brokers(ZkUtils zkUtils) {
        List<Broker> brokers = JavaConversions.seqAsJavaList(zkUtils.getAllBrokersInCluster());
        if (null == brokers || brokers.size() == 0) {
            throw new RuntimeException("broker null");
        }

        Broker broker = brokers.get(0);
        String brokerStr = broker.getBrokerEndPoint(SecurityProtocol.PLAINTEXT).connectionString();
        return brokerStr;

        // Properties properties=new Properties();
        // properties.put("bootstrap.servers",brokerStr);
        //AdminClient adminClient=AdminClient.createSimplePlaintext(brokerStr);
        //return adminClient;
    }

    public static void main(String[] args) {
        String zookeeper = "10.90.9.4:2181";

        MyAdminClient admin = new MyAdminClient(zookeeper);

//        System.out.println(admin.getAllConsumerGroupsForTopic("im_export_raw"));

//        admin.getTopicParitionOffset("im_export_raw",0);
    }
}
