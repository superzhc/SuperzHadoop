package com.github.superzhc.hadoop.flink.streaming.demo.userbehavior;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author superz
 * @create 2021/10/12 15:06
 */
public class UserBehaviorJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<UserBehavior> ds = env.addSource(new UserbehaviorSource("data/UserBehavior-50.csv"));
        DataStream<Tuple3<Long, String, Integer>> behaviorCountStream = ds.keyBy(data -> data.getUserId())
                .flatMap(new MapStateFunction());

        behaviorCountStream.print();

        env.execute("userbehavior demo");
    }
}
