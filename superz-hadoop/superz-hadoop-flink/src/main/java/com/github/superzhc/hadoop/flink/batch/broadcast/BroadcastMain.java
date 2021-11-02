package com.github.superzhc.hadoop.flink.batch.broadcast;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/29 15:58
 */
public class BroadcastMain {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple2<Integer, String>> ds = env.fromElements(Tuple2.of(1, "张三"), Tuple2.of(2, "李四"), Tuple2.of(3, "王五"), Tuple2.of(4, "赵六"));
        DataSet<Tuple2<Integer, Integer>> broadcastVar = env.fromElements(Tuple2.of(1, 28), Tuple2.of(2, 29), Tuple2.of(3, 27));

        ds.map(new RichMapFunction<Tuple2<Integer, String>, Tuple3<Integer, String, Integer>>() {

                    private Map<Integer, Integer> map = new HashMap<>();

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        List<Tuple2<Integer, Integer>> list = getRuntimeContext().getBroadcastVariable("broadcast-demo");
                        for (Tuple2<Integer, Integer> tuple2 : list) {
                            map.put(tuple2.f0, tuple2.f1);
                        }
                    }

                    @Override
                    public Tuple3<Integer, String, Integer> map(Tuple2<Integer, String> value) throws Exception {
                        return Tuple3.of(value.f0, value.f1, map.getOrDefault(value.f0, 25));
                    }
                })
                .withBroadcastSet(broadcastVar, "broadcast-demo")
                .print();

        // env.execute("dataset broadcast use");
    }
}
