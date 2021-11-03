package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.streaming.window.WindowMain;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 在 flink 1.11 之前的版本中，提供了两种生成水印（Watermark）的策略，分别是 AssignerWithPunctuatedWatermarks 和 AssignerWithPeriodicWatermarks，这两个接口都继承自 TimestampAssigner 接口。
 * 用户想使用不同的水印生成方式，则需要实现不同的接口，但是这样引发了一个问题，对于想给水印添加一些通用的、公共的功能则变得复杂，因为需要给这两个接口都同时添加新的功能，这样还造成了代码的重复。所以为了避免代码的重复，在 flink 1.11 中对 flink 的水印生成接口进行了重构。
 * 新的 Watermark 生成接口是通过实现 WatermarkStrategy 接口实现的
 *
 * @author superz
 * @create 2021/10/25 10:04
 */
public class WatermarkStrategyMain<T> {
    /**
     * 水印生成策略
     *
     * @param ds
     * @param watermarkStrategy
     * @return
     */
    public DataStream<T> assignTimestampsAndWatermark(DataStream<T> ds, WatermarkStrategy<T> watermarkStrategy) {
        return ds.assignTimestampsAndWatermarks(watermarkStrategy);
    }

    /**
     * 抽取 Event Time，并生成水印
     *
     * @param ds
     * @param watermarkStrategy
     * @param timestampAssigner
     * @return
     */
    public DataStream<T> assignTimestampsAndWatermark(DataStream<T> ds, WatermarkStrategy<T> watermarkStrategy, SerializableTimestampAssigner<T> timestampAssigner) {
        return assignTimestampsAndWatermark(ds, watermarkStrategy.withTimestampAssigner(timestampAssigner));
    }

    public DataStream<T> assignTimestampsAndWatermarkByCustom(DataStream<T> ds) {
        WatermarkStrategy<T> watermarkStrategy = new WatermarkStrategy<T>() {
            /**
             * 水印策略默认只需要实现这个接口，该接口返回 WatermarkGenerator
             * @param context
             * @return
             */
            @Override
            public WatermarkGenerator<T> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new WatermarkGenerator<T>() {
                    private long maxTimestamp;
                    private final long delay = 5000L;

                    /**
                     * 每个元素都会调用这个方法，如果想通过每个元素生成一个水印，通过实现该方法
                     * @param event
                     * @param eventTimestamp
                     * @param output
                     */
                    @Override
                    public void onEvent(T event, long eventTimestamp, WatermarkOutput output) {
                        maxTimestamp = Math.max(maxTimestamp, eventTimestamp);
                        /*output.emitWatermark(new Watermark(maxTimestamp));*/
                    }

                    /**
                     * 如果数据量比较大的时候，每条数据都生成一个水印的话，会影响性能，可以调用该方法生成一个周期性的水印。
                     * 水印的生成周期设置为：<code>env.getConfig().setAutoWatermarkInterval(5000L);</code>
                     * @param output
                     */
                    @Override
                    public void onPeriodicEmit(WatermarkOutput output) {
                        output.emitWatermark(new Watermark(maxTimestamp - delay));
                    }
                };
            }
        };

        SerializableTimestampAssigner<T> timestampAssigner = new SerializableTimestampAssigner<T>() {
            @Override
            public long extractTimestamp(T element, long recordTimestamp) {
                return recordTimestamp;
            }
        };

        return assignTimestampsAndWatermark(ds, watermarkStrategy, timestampAssigner);
    }

    /**
     * 内置策略：固定延时生成水印
     *
     * @param ds
     * @return
     */
    public DataStream<T> assignTimestampsAndWatermarkByBuildIn1(DataStream<T> ds) {
        return assignTimestampsAndWatermark(ds, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)));
    }

    /**
     * 内置策略：单调递增水印
     *
     * @param ds
     * @return
     */
    public DataStream<T> assignTimestampsAndWatermarkByBuildIn2(DataStream<T> ds) {
        return assignTimestampsAndWatermark(ds, WatermarkStrategy.forMonotonousTimestamps());
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(1000);

        Map<String, String> fakerConfig = new HashMap<>();
        fakerConfig.put("fields.name.expression", "#{Name.name}");
        fakerConfig.put("fields.age.expression", "#{number.number_between '1','80'}");
        fakerConfig.put("fields.date.expression", FakerUtils.Expression.pastDate(10));

        final Long defaultTimestamp = LocalDateTime.of(1970, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        final ObjectMapper mapper = new ObjectMapper();
        /* 2021年11月2日 superz add 注意 OutputTag 后面的大括号，这里必须这样写，直接 new 的话，Flink 报错：The types of the interface org.apache.flink.util.OutputTag could not be inferred. Support for synthetic interfaces, lambdas, and generic or raw types is limited at this point */
        final OutputTag<String> tag = new OutputTag<String>("WatermarkStrategyMainLateData") {
        };
        SingleOutputStreamOperator<String> ds = env.addSource(new JavaFakerSource(fakerConfig))
                // 抽取时间戳
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                // 设置水印的时间为事件最大事件延迟 3s
                                .<String>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                // 设置抽取时间戳，如果不设置这个将会使用默认的记录操作时间，不符合事件时间
                                .withTimestampAssigner(new SerializableTimestampAssigner<String>() {
                                    @Override
                                    public long extractTimestamp(String element, long recordTimestamp) {
                                        try {
                                            JsonNode node = mapper.readTree(element);
                                            System.out.println(node.get("id").asText() + ":" + FakerUtils.toLocalDateTime(node.get("date").asText()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                            return FakerUtils.toTimestamp(node.get("date").asText());
                                        } catch (JsonProcessingException e) {
                                            // 无法直接转换的，直接将时间戳设置为1970-01-01 00:00:00
                                            return defaultTimestamp;
                                        }
                                    }
                                })
                )
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(10)))
                .trigger(WindowMain.MyTrigger.trigger())
                .sideOutputLateData(tag)
                // 简单的每个窗口计数器，采用增量方式 ProcessWindowFunction
                .aggregate(new AggregateFunction<String, Integer, Integer>() {
                    @Override
                    public Integer createAccumulator() {
                        return 0;
                    }

                    @Override
                    public Integer add(String value, Integer accumulator) {
                        accumulator += 1;
                        return accumulator;
                    }

                    @Override
                    public Integer getResult(Integer accumulator) {
                        return accumulator;
                    }

                    @Override
                    public Integer merge(Integer a, Integer b) {
                        return a + b;
                    }
                }, new ProcessAllWindowFunction<Integer, String, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<Integer, String, TimeWindow>.Context context, Iterable<Integer> elements, Collector<String> out) throws Exception {
                        Integer counter = -1;
                        for (Integer element : elements) {
                            counter = element;
                        }
                        TimeWindow tw = context.window();
                        String start = LocalDateTime.ofInstant(Instant.ofEpochMilli(tw.getStart()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String end = LocalDateTime.ofInstant(Instant.ofEpochMilli(tw.getEnd()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        out.collect(String.format("[%s,%s]:%d", start, end, counter));
                    }
                });

        ds.print();

        DataStream<String> lateDs = ds.getSideOutput(tag);
        lateDs.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                return "Late Data:" + value;
            }
        }).print();

        env.execute("watermark strategy main");

        /**
         * 设置滚动窗口周期为 10s， 如果不存在水印，理论上每个窗口的计数是固定的 10 条，但运行程序可以明显的看出来，每个窗口的计算是条数并非固定的 10 条
         */
    }
}
