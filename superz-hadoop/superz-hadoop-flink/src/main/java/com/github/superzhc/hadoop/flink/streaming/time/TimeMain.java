package com.github.superzhc.hadoop.flink.streaming.time;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 时间
 * <p>
 * Flink 支持如下 3 中时间概念：
 * 1. Event Time：事件产生的时间，它通常由事件中的时间戳描述
 * 2. Ingestion Time：事件进入 Flink 的时间
 * 3. Processing Time：事件被处理时当前系统的时间
 * <p>
 * 注意事项：
 * 1. 从 Flink 1.12 开始，流式数据的默认时间戳从 Processing Time 变为 Event Time
 * 2. 使用 Event Time 必须指定如何生成 Event Time 水印，这是表示 Event Time 进度的机制
 *
 * @author superz
 * @create 2021/10/9 17:09
 */
public class TimeMain {
    /**
     * 事件时间
     * 如果使用 Event Time 处理流式数据，需要在 Flink 程序中指定 Event Time 时间戳信息，在 Flink 程序运行过程中会通过指定方法抽取出对应的 Event Time，该过程叫做 Timestamps Assigining 。
     * Event Time 指定完成后，需要创建对应的 Watermarks，需要用户定义根据 Timestamps 计算出 Watermarks 的生成策略。
     * 目前 Flink 支持两种方式指定 Timestamps 和生成 Watermarks，一种方式在 DataStream Source 算子接口的 SourceFunction 中定义，另外一种方式是通过自定义 Timestamp Assigner 和 Watermark Generator 生成。
     */
    static class EventTime {
        /**
         * 通过数据源定义
         *
         * @param <T>
         */
        public <T> void SourceWithTimestampsAndWatermark() {
            new SourceFunction<T>() {
                private boolean isRunning = true;

                @Override
                public void run(SourceContext<T> ctx) throws Exception {
                    while (isRunning) {
                        // 定义元素，没什么实质意义
                        T element = null;
                        /* 抽取 Event Time */
                        ctx.collectWithTimestamp(element, LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                        /* 创建 Watermark */
                        ctx.emitWatermark(new Watermark(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                    }

                    // 设置默认 Watermark
                    ctx.emitWatermark(new Watermark(Long.MAX_VALUE));
                }

                @Override
                public void cancel() {
                    isRunning = false;
                }
            };
        }

        /**
         * 通过自定义 Timestamp Assigner 和 Watermark Generator 生成
         * <p>
         * Timestamp Assigner 一般是跟在 Data Source 算子后面指定，也可以在后续的算子中指定，只要保证 Timestamp Assigner 在第一个时间相关的 Operator 之前即可。
         * 如果用户已经在 SourceFunciton 中定义 Timestamps 和 Watermarks 的生成逻辑，同时又使用了 Timestamp Assigner，此时 Assigner 会覆盖 SourceFunction 中定义的逻辑。
         */
        public <T> DataStream<T> timestampAssigner(DataStream<T> ds) {
            return ds;
        }
    }

    /**
     * 事件进入 Flink 的事件
     */
    static class IngestionTime {
    }

    /**
     * 事件被处理时的系统时间
     */
    static class ProcessingTime {
    }
}
