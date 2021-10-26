package com.github.superzhc.hadoop.flink.streaming.connector.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Kafka数据源
 */
public class KafkaSourceDemo {
    private static final Logger log = LoggerFactory.getLogger(KafkaSourceDemo.class);

    private static final String BROKERS = "127.0.0.1:19092";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //设置并行度（使用几个CPU核心）
//        env.setParallelism(2);
//        //每隔2000ms进行启动一个检查点
//        env.enableCheckpointing(2000);
//        //高级选项：
//        //设置模式为exactly-once
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        //确保检查点之间至少有500ms的间隔（CheckPoint最小间隔）
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//        //检查点必须在1min内完成，或者被丢弃（CheckPoint的超时时间）
//        env.getCheckpointConfig().setCheckpointTimeout(60000);
//        //同一时间只允许操作一个检查点
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//        //表示一旦Flink处理程序被cancel后，会保留CheckPoint数据，以便根据实际需要恢复到指定的CheckPoint
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //1.消费者客户端连接到kafka
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-flink-user:superz");
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "superz-test"
                , new SimpleStringSchema()
                , props);
        consumer.setStartFromLatest();

        //2.在算子中进行处理
        DataStream<String> ds = env.addSource(consumer);
        ds.print();

        //3.执行
        env.execute("flink  kafka source");
    }
}
