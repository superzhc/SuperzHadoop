package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * 2020年11月25日 superz add
 */
@Deprecated
public class JavaCustomAssignerWithPeriodicWatermarks
{
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 使用AssignerWithPeriodicWaters接口之前，必须先配置Watermarks产生的时间周期
        env.getConfig().setAutoWatermarkInterval(200);
    }

    @Deprecated
    static class PeriodicAssigner implements AssignerWithPeriodicWatermarks<Tuple3<String, Long, Integer>>
    {
        // 1s时延设定，表示在1s以内的数据延时都是有效，超过1s的数据被认定为迟到事件
        private Long maxOutOfOrderness = 1000L;
        private Long currentMaxTimestamp = 0L;

        /**
         * 定义了生成Watermark的逻辑
         * @return
         */
        @Override
        public Watermark getCurrentWatermark() {
            // 根据最大事件时间减去最大的乱序时延延长度，然后得到Watermark
            return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
        }

        /**
         * 定义了抽取Timestamp的逻辑
         * @param element
         * @param recordTimestamp
         * @return
         */
        @Override
        public long extractTimestamp(Tuple3<String, Long, Integer> element, long recordTimestamp) {
            Long currentTimstamp = element.f1;
            // 对比当前的事件时间和历史最大事件时间，将最新的时间赋值给currentMaxTimestamp变量
            currentMaxTimestamp = Math.max(currentTimstamp, currentMaxTimestamp);
            return currentTimstamp;
        }
    }
}
