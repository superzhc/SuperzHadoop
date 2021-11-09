package com.github.superzhc.hadoop.flink.streaming;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Flink DataStream API 为用户提供了 3 个算子来实现双流 join，分别是：
 * 1. join()
 * 2. coGroup()
 * 3. intervalJoin()
 *
 * @author superz
 * @create 2021/11/9 10:07
 */
public class JavaDataStreamJoinMain {
    private static <T, V> void join(DataStream<T> stream, DataStream<V> otherStream) {
        /**
         * join 算子提供的语义为 Window join，即按照指定字段和（滚动/滑动/会话）窗口进行 inner join，支持 Processing Time 和 Event Time 时间特征。
         *
         * 语法：
         * stream.join(otherStream)
         *     .where(<KeySelector>)
         *     .equalTo(<KeySelector>)
         *     .window(<WindowAssigner>)
         *     .apply(<JoinFunction>)
         */

        stream.join(otherStream)
                .where(new KeySelector<T, Object>() {
                    @Override
                    public Object getKey(T value) throws Exception {
                        return value;
                    }
                })
                .equalTo(new KeySelector<V, Object>() {
                    @Override
                    public Object getKey(V value) throws Exception {
                        return value;
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .apply(new JoinFunction<T, V, Object>() {
                    @Override
                    public Object join(T first, V second) throws Exception {
                        return null;
                    }
                });


    }

    private static <T, V> void coGroup(DataStream<T> stream, DataStream<V> otherStream) {
        /**
         * coGroup
         */

        stream.coGroup(otherStream)
                .where(new KeySelector<T, Object>() {
                    @Override
                    public Object getKey(T value) throws Exception {
                        return value;
                    }
                })
                .equalTo(new KeySelector<V, Object>() {
                    @Override
                    public Object getKey(V value) throws Exception {
                        return value;
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .apply(new CoGroupFunction<T, V, Object>() {
                    @Override
                    public void coGroup(Iterable<T> first, Iterable<V> second, Collector<Object> out) throws Exception {
                        /* 简单实现 left join 的功能 */
                        for (T left : first) {
                            boolean isMatched = false;
                            for (V right : second) {
                                if (left.equals(right)) {
                                    out.collect(Tuple2.of(left, right));
                                    isMatched = true;
                                }
                            }

                            if (!isMatched) {
                                out.collect(Tuple2.of(left, null));
                            }
                        }
                    }
                });
    }

    private static <T, V> void intervalJoin(DataStream<T> stream, DataStream<V> otherStream) {
        /**
         * interval join
         *
         *  interval join 按照指定字段以及<b>右流相对左流偏移的时间区间进行关联</b>，即：
         *
         *  left.timestamp + lowerBound <= right.timestamp <= left.timestamp.upperBound
         *
         *  interval join 也是 inner join，虽然不需要开窗，但是需要用户指定偏移区间的上下界，并且只支持<b>事件时间</b>
         *
         *  注意事项：
         *  1. 因为只支持事件事件，需要对应两个流都应用 assignTimestampsAndWatermarks() 方法获取事件时间戳和水印
         */

        DataStream<T> streamWithTimestampsAndWatermarks = stream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<T>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner(new SerializableTimestampAssigner<T>() {
                            @Override
                            public long extractTimestamp(T element, long recordTimestamp) {
                                return 0;
                            }
                        })
        );
        DataStream<V> otherStreamWithTimestampsAndWatermarks = otherStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<V>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner(new SerializableTimestampAssigner<V>() {
                            @Override
                            public long extractTimestamp(V element, long recordTimestamp) {
                                return 0;
                            }
                        })
        );

        /**
         * interval join 与 window join 不同，是两个 KeyedStream 之上的操作，并且调用 between() 方法指定偏移区间的上下界。如果想令上下界是开区间，可以调用 upperBoundExclusive()/lowerBoundExclusive() 方法
         */
        streamWithTimestampsAndWatermarks.keyBy(new KeySelector<T, Object>() {
                    @Override
                    public Object getKey(T value) throws Exception {
                        return value;
                    }
                })
                .intervalJoin(otherStream.keyBy(new KeySelector<V, Object>() {
                    @Override
                    public Object getKey(V value) throws Exception {
                        return value;
                    }
                }))
                .between(Time.seconds(-30), Time.seconds(30))
                .process(new ProcessJoinFunction<T, V, Object>() {
                    @Override
                    public void processElement(T left, V right, ProcessJoinFunction<T, V, Object>.Context ctx, Collector<Object> out) throws Exception {

                    }
                });
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> fakerConfig1 = new HashMap<>();
        fakerConfig1.put(JavaFakerSource.field("name"), FakerUtils.Expression.name());
        fakerConfig1.put(JavaFakerSource.field("age"), FakerUtils.Expression.age(18, 60));
        JavaFakerSource fakerSource1 = new JavaFakerSource(fakerConfig1);

        Map<String, String> fakerConfig2 = new HashMap<>();
        fakerConfig2.put(JavaFakerSource.field("name"), FakerUtils.Expression.name());
        fakerConfig2.put(JavaFakerSource.field("sex"), FakerUtils.Expression.options("男", "女"));
        JavaFakerSource fakerSource2 = new JavaFakerSource(fakerConfig2);

        DataStream<String> stream = env.addSource(fakerSource1);
        DataStream<String> otherStream = env.addSource(fakerSource2);

        final ObjectMapper mapper = new ObjectMapper();

        /* inner join */
//        stream.join(otherStream)
//                .where(new KeySelector<String, String>() {
//                    @Override
//                    public String getKey(String value) throws Exception {
//                        JsonNode node = mapper.readTree(value);
//                        return node.get("name").asText();
//                    }
//                })
//                .equalTo(value -> mapper.readTree(value).get("name").asText())
//                .window(TumblingProcessingTimeWindows.of(Time.seconds(60)))
//                .apply(new JoinFunction<String, String, String>() {
//                    @Override
//                    public String join(String first, String second) throws Exception {
//                        ObjectNode result = mapper.createObjectNode();
//
//                        JsonNode firstNode = mapper.readTree(first);
//                        JsonNode secondNode = mapper.readTree(second);
//
//                        result.put("name", firstNode.get("name").asText());
//                        result.put("age", firstNode.get("age").asInt());
//                        result.put("sex", secondNode.get("sex").asText());
//
//                        return mapper.writeValueAsString(result);
//                    }
//                })
//                .print()
//        ;

        /* left join */
        stream.coGroup(otherStream)
                .where(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        JsonNode node = mapper.readTree(value);
                        return node.get("name").asText();
                    }
                })
                .equalTo(value -> mapper.readTree(value).get("name").asText())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(60)))
                .apply(new CoGroupFunction<String, String, String>() {
                    @Override
                    public void coGroup(Iterable<String> first, Iterable<String> second, Collector<String> out) throws Exception {
                        for (String firstItem : first) {

                            JsonNode firstNode = mapper.readTree(firstItem);
                            Boolean isMatch = false;

                            for (String secondItem : second) {
                                JsonNode secondNode = mapper.readTree(secondItem);
                                if (firstNode.get("name").asText().equals(secondNode.get("name").asText())) {
                                    isMatch = true;
                                    ObjectNode result = mapper.createObjectNode();
                                    result.put("name", firstNode.get("name").asText());
                                    result.put("age", firstNode.get("age").asInt());
                                    result.put("sex", secondNode.get("sex").asText());
                                    out.collect(mapper.writeValueAsString(result));
                                }
                            }

                            if (!isMatch) {
                                ObjectNode result = mapper.createObjectNode();
                                result.put("name", firstNode.get("name").asText());
                                result.put("age", firstNode.get("age").asInt());
                                out.collect(mapper.writeValueAsString(result));
                            }


                        }
                    }
                })
                .print();

        /* right join，同上 */
        /* outer join，同上 */

        env.execute("java datastream join main");
    }
}
