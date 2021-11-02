package com.github.superzhc.hadoop.flink;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * StreamGraph
 * StreamGraph 是根据用户编写的代码生成的最初的 Graph，它完全是在 Client 端生成的。
 * StreamGraph 经过优化转换为 JobGraph，Client 端向 JobManager 提交的作业就是以 JobGraph 来提交的。
 * @author superz
 * @create 2021/11/1 14:24
 */
public class StreamGraphMain {
    public static void main(String[] args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String,String> fakerConfig=new HashMap<>();
        fakerConfig.put("name", FakerUtils.Expression.name());
        fakerConfig.put("age",FakerUtils.Expression.age(1,80));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfig));
        ds.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                return value;
            }
        }).flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                out.collect(value);
            }
        }).print();

        /* 获取 StreamGraph */
        String streamGraph=env.getStreamGraph().getStreamingPlanAsJSON();
        System.out.println(streamGraph);

        /**
         结果：
         {
             "nodes" : [ {
                 "id" : 1,
                 "type" : "Source: Custom Source",
                 "pact" : "Data Source",
                 "contents" : "Source: Custom Source",
                 "parallelism" : 1
             }, {
                 "id" : 2,
                 "type" : "Map",
                 "pact" : "Operator",
                 "contents" : "Map",
                 "parallelism" : 8,
                 "predecessors" : [ {
                     "id" : 1,
                     "ship_strategy" : "REBALANCE",
                     "side" : "second"
             } ]
         }, {
             "id" : 3,
             "type" : "Flat Map",
             "pact" : "Operator",
             "contents" : "Flat Map",
             "parallelism" : 8,
             "predecessors" : [ {
                 "id" : 2,
                 "ship_strategy" : "FORWARD",
                 "side" : "second"
             } ]
         }, {
             "id" : 4,
             "type" : "Sink: Print to Std. Out",
             "pact" : "Data Sink",
             "contents" : "Sink: Print to Std. Out",
             "parallelism" : 8,
             "predecessors" : [ {
                 "id" : 3,
                 "ship_strategy" : "FORWARD",
                 "side" : "second"
             } ]
            }]
         }
         */

        /* 查看 StreamGraph 的可视化：https://flink.apache.org/visualizer/ */
    }
}
