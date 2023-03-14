package com.github.superzhc.hadoop.flink.streaming.demo;

import com.github.superzhc.data.news.MoFish;
import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaProducerSource;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.RedisSink;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.config.FlinkJedisPoolConfig;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.mapper.RedisCommand;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.mapper.RedisCommandDescription;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.mapper.RedisMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/22 16:01
 **/
public class FlinkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//        DataStream<Map<String, Object>> ds = env.addSource(
//                new JavaProducerSource<String>(
//                        new JavaProducerSource.Producer<List<Map<String, Object>>>() {
//                            @Override
//                            public List<Map<String, Object>> get() {
//                                return MoFish.taobao();
//                            }
//                        }
//                        , new JavaProducerSource.IdentifyValidate<>("id")
//                ));
//        ds.print();

//        KafkaSink<String> sink = KafkaSink.<String>builder()
//                .setBootstrapServers("10.90.15.142:9092,10.90.15.221:9092,10.90.15.233:9092")
//                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
//                        .setTopic("t_913362763035197440_n_mo_fish")
//                        .setValueSerializationSchema(new SimpleStringSchema())
//                        .build()
//                )
//                // .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
//                .build();
//        ds.map(d -> JsonUtils.asString(d)).sinkTo(sink);

        FlinkJedisPoolConfig flinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost("127.0.0.1")
                .setPort(6379)
                .build();

        RedisSink<Map<String, String>> sink = new RedisSink<>(
                flinkJedisPoolConfig,
                new RedisMapper<Map<String, String>>() {
                    @Override
                    public RedisCommandDescription getCommandDescription() {
                        return new RedisCommandDescription(RedisCommand.HSET, "test");
                    }

                    @Override
                    public String getKeyFromData(Map<String, String> data) {
                        return "title";
                    }

                    @Override
                    public String getValueFromData(Map<String, String> data) {
                        return data.get("Title");
                    }

                    @Override
                    public Optional<String> getAdditionalKey(Map<String, String> data) {
                        return Optional.ofNullable(data.get("id"));
                    }
                }
        );

//        ds.addSink(sink);

        env.execute("java producer source");
    }
}
