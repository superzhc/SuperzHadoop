package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020年11月24日 superz add
 */
public class JavaEventTimeAndWatermark
{
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<Tuple3<String, Long, Integer>> input = new ArrayList<>();
        input.add(new Tuple3("a", 1L, 1));
        input.add(new Tuple3("b", 1L, 1));
        input.add(new Tuple3("b", 3L, 1));

        DataStreamSource<Tuple3<String, Long, Integer>> source = env
                .addSource(new SourceFunction<Tuple3<String, Long, Integer>>()
                {
                    @Override
                    public void run(SourceContext<Tuple3<String, Long, Integer>> ctx) throws Exception {
                        for (Tuple3<String, Long, Integer> value : input) {
                            // 调用collectionWithTimestamp增加Event Time抽取
                            ctx.collectWithTimestamp(value, value.f1);

                            // 调用emitWaterTimestamp，创建Watermark，最大延时设定为1
                            ctx.emitWatermark(new Watermark(value.f1 - 1));
                        }

                        // 设定默认Watermark
                        ctx.emitWatermark(new Watermark(Long.MAX_VALUE));
                    }

                    @Override
                    public void cancel() {

                    }
                });
    }
}
