package com.github.superzhc.hadoop.flink.streaming.window;

import com.github.superzhc.hadoop.flink.streaming.connector.kafka.KafkaConnectorMain;
import com.github.superzhc.hadoop.flink.streaming.demo.fund.FlinkFundSource;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 窗口
 * <p>
 * Flink 支持两种划分窗口的方式：
 * 1. 根据时间划分：time-window
 * 2. 根据数量划分：count-window
 * <p>
 * Flink 支持的窗口有两个属性（size 和 interval）
 * 1. 如果 `size=interval`,那么就会形成 tumbling-window(无重叠数据)
 * 2. 如果 `size>interval`,那么就会形成 sliding-window(有重叠数据)
 * 3. 如果 `size<interval`,那么这种窗口将会丢失数据。比如每5秒钟，统计过去3秒的通过路口汽车的数据，将会漏掉2秒钟的数据。
 * <p>
 * 通过上面的组合，可以获取如下四种基本窗口：
 * 1. time-tumbling-window 无重叠数据的时间窗口，设置方式举例：timeWindow(Time.seconds(5))
 * 2. time-sliding-window 有重叠数据的时间窗口，设置方式举例：timeWindow(Time.seconds(5), Time.seconds(3))
 * 3. count-tumbling-window无重叠数据的数量窗口，设置方式举例：countWindow(5)
 * 4. count-sliding-window 有重叠数据的数量窗口，设置方式举例：countWindow(5,3)
 *
 * @author superz
 * @create 2021/10/9 16:43
 */
public class WindowMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<ObjectNode> ds = env.addSource(KafkaConnectorMain.sourceWithJson("superz-" + WindowMain.class.getSimpleName(), "flink-test2"))
                .map(data -> {
                    ObjectNode newData = data.get("value").deepCopy();
                    return newData;
                });
        ds = ds
                .keyBy(new KeySelector<ObjectNode, String>() {
                    @Override
                    public String getKey(ObjectNode value) throws Exception {
                        String content = value.get("content").asText();
                        if (null != content && content.length() > 0) {
                            String heroInfo = content.split(" ")[0];
                            return heroInfo.split("-")[1];
                        }
                        return null;
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.minutes(5)))
                .reduce(new ReduceFunction<ObjectNode>() {
                    @Override
                    public ObjectNode reduce(ObjectNode value1, ObjectNode value2) throws Exception {
                        /*String content = value1.get("content").asText("") +"\n"+ value2.get("content").asText("");
                        return value1.put("content", content);*/
                        String eventTimes = value1.get("event_time").asText("") + "," + value2.get("event_time").asText("");
                        return value1.put("event_time", eventTimes);
                    }
                });

        ds.print();

        env.execute(WindowMain.class.getName());
    }

    /**
     * 全部的流数据
     *
     * @param ds
     * @param <T>
     */
    public <T> void allWindowStruct(DataStream<T> ds) {
        ds
                .windowAll(null)           // 指定窗口分配器类型，定义如何将数据流分配到一个或多个窗口
                .trigger(null)                      // 指定触发器类型，定义窗口满足什么样的条件触发计算（可选）
                .evictor(null)                      // 指定 evictor，用于数据剔除（可选）
                .allowedLateness(null)              // 标记是否处理迟到数据，当迟到数据到达窗口中是否触发计算（可选）
                .sideOutputLateData(null)           // 指定 Output Lag，标记输出标签，然后再通过 getSideOutput 将窗口中的数据根据标签输出（可选）
                .apply(null)               // 指定窗口计算函数
                .getSideOutput(null) // 指定 Tag 输出数据（可选）
        ;
    }

    /**
     * key-value 流数据
     *
     * @param ks
     * @param <K>
     * @param <V>
     */
    public <K, V> void keyedWindowStruct(KeyedStream<K, V> ks) {
        ks
                //.window(null)              // 指定窗口分配器类型，定义如何将数据流分配到一个或多个窗口
                .window(TimeTumblingWindow.tumblingEventTime(10L))
                .trigger(null)                      // 指定触发器类型，定义窗口满足什么样的条件触发计算（可选）
                .evictor(null)                      // 指定 evictor，用于数据剔除（可选）
                .allowedLateness(null)              // 标记是否处理迟到数据，当迟到数据到达窗口中是否触发计算（可选）
                .sideOutputLateData(null)           // 指定 Output Lag，标记输出标签，然后再通过 getSideOutput 将窗口中的数据根据标签输出（可选）
                // .apply(null)               // 指定窗口计算函数
                .aggregate(MyAggregateFunction.aggregate())
                .getSideOutput(null) // 指定 Tag 输出数据（可选）
        ;
    }

    // region 窗口类型

    /**
     * 基于时间的滑动窗口
     */
    static class TimeTumblingWindow {
        public static WindowAssigner<Object, TimeWindow> tumblingEventTime(Long size) {
            /* of 用于定义窗口的大小 */
            return TumblingEventTimeWindows.of(Time.seconds(size));
        }

        public static WindowAssigner<Object, TimeWindow> tumblingProcessingTime(Long size) {
            return TumblingProcessingTimeWindows.of(Time.seconds(size));
        }
    }

    static class TimeSlidingWindow {
        public WindowAssigner<Object, TimeWindow> slidingEventTime() {
            /* of 用于定义窗口的大小 */
            return SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5));
        }

        public WindowAssigner<Object, TimeWindow> slidingProcessingTime() {
            return SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5));
        }
    }

    static class CountTumblingWindow {
    }

    static class CountSlidingWindow {
    }
    // endregion

    // region 窗口函数
    static class MyReduceFunction {
        public static <T> ReduceFunction<T> reduce() {
            return new ReduceFunction<T>() {
                @Override
                public T reduce(T value1, T value2) throws Exception {
                    return value1;
                }
            };
        }
    }

    static class MyAggregateFunction {
        public static <T> AggregateFunction<T/*输入数据类型*/, Long/*中间结果类型*/, Long/*输出结果类型*/> aggregate() {
            return new AggregateFunction<T, Long, Long>() {

                @Override
                public Long createAccumulator() {
                    return 0L;
                }

                @Override
                public Long add(T value, Long accumulator) {
                    return accumulator++;
                }

                @Override
                public Long getResult(Long accumulator) {
                    return accumulator;
                }

                @Override
                public Long merge(Long a, Long b) {
                    return a + b;
                }
            };
        }
    }

    /**
     * FoldFunction 已经在 Flink DataStream API 中被标记为 `@Deprecated`，也就是说很可能会在未来的版本中移除，Flink 建议用户使用 AggregateFunction 来替换使用 FoldFunction。
     */
    @Deprecated
    static class MyFoldFunction {
    }

    static class MyProcessWindowFunction {
        public static <In, Out, K> ProcessWindowFunction<In, Out, K, TimeWindow> process() {
            return new ProcessWindowFunction<In, Out, K, TimeWindow>() {
                @Override
                public void process(K k, ProcessWindowFunction<In, Out, K, TimeWindow>.Context context, Iterable<In> elements, Collector<Out> out) throws Exception {

                }
            };
        }
    }
    // endregion

    // region 窗口起始时间
    /**
     * Flink 中窗口的计算时间不是根据进入窗口的第一个元素记为窗口的开始时间和加 Size 记为窗口结束时间，而是根据 Flink 内置计算公式：
     * timestamp - (timestamp - offset + windowSize) % windowSize
     */

    /**
     * 注意事项：
     * 1. Flink 的窗口开始时间都是按照整点来统计的，比如：当前时间是 17:28，按照每 30 分钟统计一次，那么统计的开始时间就是 [2021-11-02 17:00:00,2021-11-02 17:30:00]
     * 2. Flink 如果按天来统计数据，则需要考虑时区的问题会造成从早晨8点开始统计，需要进行 offset 设置：.window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(16)))，详细见：https://blog.csdn.net/maomaoqiukqq/article/details/104334993
     */
    // endregion

    // region 窗口触发器

    /**
     * 数据接入窗口后，窗口是否触发 Window Funciton 计算，取决于窗口是否满足触发条件，每种类型的窗口都有对应的窗口触发机制，保障每一次接入窗口的数据都能够按照规定的触发逻辑进行统计计算。
     * Flink 在内部定义了窗口触发器来控制窗口的触发机制，分别有 EventTimeTrigger、ProcessTimeTrigger 以及 CountTrigger 等。
     */
    static class Trigger {
        /**
         * 每一个窗口都拥有一个属于自己的 Trigger，Trigger 上会有定时器，用来决定一个窗口何时能够被计算或清除。
         * 每当有元素加入到该窗口，或者之前注册的定时器超时了，那么 Trigger 都会被调用。
         * Trigger 的返回结果可以是 continue（不做任何操作），fire（处理窗口数据），purge（移除窗口和窗口中的数据），或者 fire + purge。
         * 一个 Trigger 的调用结果只是 fire 的话，那么会计算窗口并保留窗口原样，也就是说窗口中的数据仍然保留不变，等待下次 Trigger fire 的时候再次执行计算。
         * 一个窗口可以被重复计算多次直到它被 purge 了。在 purge 之前，窗口会一直占用着内存。
         *
         * 当 Trigger fire 了，窗口中的元素集合就会交给Evictor（如果指定了的话）。
         * Evictor 主要用来遍历窗口中的元素列表，并决定最先进入窗口的多少个元素需要被移除。剩余的元素会交给用户指定的函数进行窗口的计算。如果没有 Evictor 的话，窗口中的所有元素会一起交给函数进行计算。
         */
    }
    // endregion

    // region 迟到数据处理

    /**
     * 目前Flink有三种处理迟到数据的方式：
     * 1. 直接将迟到数据丢弃【默认情况】
     * 2. 将迟到数据发送到另一个流
     * 3. 重新执行一次计算，将迟到数据考虑进来，更新计算结果
     */

    /* 将迟到数据发送到另一个流 */
    public <T, K, W extends Window> WindowedStream<T, K, W> sideOutputLateDate(WindowedStream<T, K, W> ws) {
        return ws.sideOutputLateData(new OutputTag<>("late-element"));
    }

    /* 将迟到数据发送到另一个流 */
    public <T, W extends Window> AllWindowedStream<T, W> sideOutputLateDate(AllWindowedStream<T, W> ws) {
        return ws.sideOutputLateData(new OutputTag<>("late-element"));
    }

    /* 重新执行一次计算，将迟到数据考虑进来，更新计算结果 */
    public <T, K, W extends Window> DataStream<String> allowedLateness(WindowedStream<T, K, W> ws) {
        return ws
                .allowedLateness(Time.seconds(5))
                .process(new ProcessWindowFunction<T, String, K, W>() {
                    @Override
                    public void process(K k, ProcessWindowFunction<T, String, K, W>.Context context, Iterable<T> elements, Collector<String> out) throws Exception {
                        // 是否被迟到数据更新
                        ValueState<Boolean> isUpdated = context.windowState().getState(new ValueStateDescriptor<Boolean>("isUpdated", Boolean.class));

                        int count = 0;
                        for (T element : elements) {
                            count++;
                        }

                        if (!isUpdated.value()) {
                            out.collect("first:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + count);
                            isUpdated.update(true);
                        } else {
                            out.collect("update:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + count);
                        }
                    }
                })
                ;
    }

    /* 重新执行一次计算，将迟到数据考虑进来，更新计算结果 */
    public <T, W extends Window> DataStream<String> allowedLateness(AllWindowedStream<T, W> ws) {
        return ws
                // 调用 allowedLateness 设置延迟时间，如果不明确调用，则默认的允许延迟参数是 0。
                // 注意事项：如果对一个 Processing Time 的程序使用 allowedLateness，将引发异常
                .allowedLateness(Time.seconds(5))
                .process(new ProcessAllWindowFunction<T, String, W>() {
                    @Override
                    public void process(ProcessAllWindowFunction<T, String, W>.Context context, Iterable<T> elements, Collector<String> out) throws Exception {
                        // 是否被迟到数据更新
                        ValueState<Boolean> isUpdated = context.windowState().getState(new ValueStateDescriptor<Boolean>("isUpdated", Boolean.class));

                        int count = 0;
                        for (T element : elements) {
                            count++;
                        }

                        if (!isUpdated.value()) {
                            out.collect("first:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + count);
                            isUpdated.update(true);
                        } else {
                            out.collect("update:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + count);
                        }
                    }
                })
                ;
    }
    // endregion
}
