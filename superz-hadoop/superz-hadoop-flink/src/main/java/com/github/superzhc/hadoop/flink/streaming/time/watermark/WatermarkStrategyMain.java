package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.time.Duration;

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
}
