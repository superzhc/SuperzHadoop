package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Watermark（水印）
 * 实际问题：
 * 在介绍Watermark相关内容之前我们先抛出一个具体的问题，在实际的流式计算中数据到来的顺序对计算结果的正确性有至关重要的影响，比如：某数据源中的某些数据由于某种原因(如：网络原因，外部存储自身原因)会有5秒的延时，也就是在实际时间的第1秒产生的数据有可能在第5秒中产生的数据之后到来(比如到Window处理节点)
 * <p>
 * Watermark是Apache Flink为了处理 EventTime 窗口计算提出的一种机制,本质上也是一种<b>时间戳</b>，由Apache Flink Source或者自定义的Watermark生成器按照需求Punctuated或者Periodic两种方式生成的一种系统Event，与普通数据流Event一样流转到对应的下游算子，接收到Watermark Event的算子以此不断调整自己管理的EventTime clock。 Apache Flink 框架保证Watermark单调递增，算子接收到一个Watermark时候，框架知道不会再有任何小于该Watermark的时间戳的数据元素到来了，所以Watermark可以看做是告诉Apache Flink框架数据流已经处理到什么位置(时间维度)的方式。
 * Apache Flink 框架保证Watermark单调递增，算子接收到一个Watermark时候，框架知道不会再有任何小于该Watermark的时间戳的数据元素到来了，所以Watermark可以看做是告诉Apache Flink框架数据流已经处理到什么位置(时间维度)的方式。
 *
 * @author superz
 * @create 2021/10/9 18:20
 */
public class WatermarkMain {
    /**
     * 目前Apache Flink 有两种生产Watermark的方式，如下：
     * 1. Punctuated - 数据流中每一个递增的EventTime都会产生一个Watermark。
     * 在实际的生产中Punctuated方式在TPS很高的场景下会产生大量的Watermark在一定程度上对下游算子造成压力，所以只有在实时性要求非常高的场景才会选择Punctuated的方式进行Watermark的生成。
     * 2. Periodic - 周期性的（一定时间间隔或者达到一定的记录条数）产生一个Watermark。
     * 在实际的生产中Periodic的方式必须结合时间和积累条数两个维度继续周期性产生Watermark，否则在极端情况下会有很大的延时。
     */

    // region 内置实现
    /* Ascending Timestamp Assigner（升序模式），会将数据中的 Timestamp 根据指定字段提取，并用当前的 Timestamp 作为最新的 Watermark，这种 Timestamp Assigner 比较适合于事件按顺序生成，没有乱序事件的情况 */
    @Deprecated /* AscendingTimestampExtractor 是一个废弃的存根类，不推荐使用 */
    public <T> DataStream<T> ascendingTimeAssigner(DataStream<T> ds) {
        ds = ds.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<T>() {
            @Override
            public long extractAscendingTimestamp(T element) {
                return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            }
        });
        return ds;
    }

    /* 使用固定时延间隔的 Timestamp Assigner 指定 Timestamps 和 Watermarks */
    public <T> DataStream<T> boundedOutOfOrdernessTimestampExtractor(DataStream<T> ds) {
        ds = ds.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<T>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(T element) {
                return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            }
        });
        return ds;
    }

    /* 自定义实现 Periodic Watermark */
    /*
     * 1. 周期性地触发 Watermark 的生成和发送，通过 ExecutionConfig.setAutoWatermarkInterval 配置
     * 2. 每隔一定周期调用 getCurrentWatermark 方法，如果得到的 Watermark 不为空且比之前大，则自动向流数据中注入一个 Watermark 事件
     *  */
    public <T> DataStream<T> customPeriodicWatermark(StreamExecutionEnvironment env, DataStream<T> ds) {
        /* 注意事项：自定义 Periodic Watermark 在使用之前需要先在 ExecutionConfig 中调用 setAutoWatermarkInterval 方法设置 Watermark 产生的时间周期 */
        env.getConfig().setAutoWatermarkInterval(1000L);

        ds = ds.assignTimestampsAndWatermarks(
                new AssignerWithPeriodicWatermarks<T>() {
                    /* 定义最大延时 */
                    private Long maxOutOfOrdernewss = 1000L;

                    /* 到目前为止最大的时间戳 */
                    private Long currentMaxTimestamp = 0L;

                    /**
                     * 定义了生成 Watermark 的逻辑
                     * @return
                     */
                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        return new Watermark(currentMaxTimestamp - maxOutOfOrdernewss);
                    }

                    /**
                     * 定义了抽取 Timestamp 的逻辑
                     * @param element
                     * @param recordTimestamp
                     * @return
                     */
                    @Override
                    public long extractTimestamp(T element, long recordTimestamp) {
                        Long currentTimestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();//element.[数据中的时间戳信息]
                        currentMaxTimestamp = Math.max(currentTimestamp, currentMaxTimestamp);
                        return currentTimestamp;
                    }
                }
        );

        return ds;
    }

    /* 自定义实现 Punctuated Watermark */
    /*
    * 1. 基于某种事件触发 Watermark 的生成和发送
    * 2. 基于事件向流数据注入 Watermark 则每个事件都会去判断是否生成一个 Watermark
    * */
    public <T> DataStream<T> customPunctuatedWatermark(DataStream<T> ds) {
        ds = ds.assignTimestampsAndWatermarks(
                new AssignerWithPunctuatedWatermarks<T>() {
                    /**
                     * 判断当前元素是否符合某个条件，如果符合更新水印，不符合直接返回null
                     * @param lastElement
                     * @param extractedTimestamp
                     * @return
                     */
                    @Nullable
                    @Override
                    public Watermark checkAndGetNextWatermark(T lastElement, long extractedTimestamp) {
                        if (null != lastElement) {
                            return new Watermark(extractedTimestamp);
                        } else {
                            return null;
                        }
                    }

                    /**
                     * 定义了抽取逻辑
                     * @param element
                     * @param recordTimestamp
                     * @return
                     */
                    @Override
                    public long extractTimestamp(T element, long recordTimestamp) {
                        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();//element.[数据中的时间戳信息];
                    }
                }
        );
        return ds;
    }
    // endregion
}
