package com.github.superzhc.hadoop.flink.streaming.window;

import com.github.superzhc.hadoop.flink.streaming.connector.kafka.KafkaConnectorMain;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

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
}
