package com.github.superzhc.flink.streaming.watermark;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * 2020年11月25日 superz add
 */
public class JavaCustomAssignerWithPunctuatedWatermarks
{
    public static void main(String[] args) {

    }

    @Deprecated
    static class PunctuatedAssigner implements AssignerWithPunctuatedWatermarks<Tuple3<String, Long, Integer>>
    {
        @Override
        public Watermark checkAndGetNextWatermark(Tuple3<String, Long, Integer> lastElement, long extractedTimestamp) {
            // 根据元素中第三位字段状态是否为0生成Watermark
            if (lastElement.f2 == 0)
                return new Watermark(extractedTimestamp);
            else
                return null;
        }

        @Override
        public long extractTimestamp(Tuple3<String, Long, Integer> element, long recordTimestamp) {
            return element.f1;
        }
    }
}
