package com.github.superzhc.hadoop.flink.streaming.operator;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * ProcessFunction 详解
 *
 * @author superz
 * @create 2021/11/9 19:29
 */
public class ProcessFunctionMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        // final ObjectMapper mapper = new ObjectMapper();
        ds.process(processFunction()).print();

        env.execute("process function main");
    }

    /**
     * Low-Level Transformation
     * @return
     */
    private static ProcessFunction<String, String> processFunction() {
        return new ProcessFunction<String, String>() {
            private Long counter = 0L;

            /**
             * 流中的每一个元素都会调用这个方法
             * @param value ：输入事件
             * @param ctx : Context 可以访问元素的时间戳，元素的 key，以及 TimerService 时间服务；Context还可以将结果输出到别的流(sideoutputs)
             * @param out ：输出结果的集合
             * @throws Exception
             */
            @Override
            public void processElement(String value, ProcessFunction<String, String>.Context ctx, Collector<String> out) throws Exception {
                out.collect(counter++ + ":" + value);
            }

            /**
             * 该函数是回调函数，当之前注册的定时触发时调用
             * @param timestamp ：定时器所设定的触发的时间戳
             * @param ctx ： 提供了上下文的一些信息，例如定时器触发的时间信息(事件时间或者处理时间)
             * @param out ：输出结果的集合
             * @throws Exception
             */
            @Override
            public void onTimer(long timestamp, ProcessFunction<String, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
            }
        };
    }

    private static KeyedProcessFunction<String, String, String> keyedProcessFunction() {
        return null;
    }

    private static CoProcessFunction<String, String, String> coProcessFunction() {
        return null;
    }

    private static ProcessJoinFunction<String, String, String> processJoinFunction() {
        return null;
    }

    private static BroadcastProcessFunction<String, String, String> broadcastProcessFunction() {
        return null;
    }

    private static KeyedBroadcastProcessFunction<String, String, String, String> keyedBroadcastProcessFunction() {
        return null;
    }

    private static ProcessWindowFunction<String, String, String, TimeWindow> processWindowFunction() {
        return null;
    }

    private static ProcessAllWindowFunction<String, String, TimeWindow> processAllWindowFunction() {
        return null;
    }
}
