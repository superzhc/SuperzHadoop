package com.github.superzhc.hadoop.flink2.streaming.job;

import com.github.superzhc.data.shopping.SMZDM;
import com.github.superzhc.hadoop.flink2.streaming.connector.common.FunctionSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/2 1:04
 */
public class SmzdmJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        FunctionSource<List<Map<String, Object>>> source = FunctionSource.<List<Map<String, Object>>>staticFunctionSource(SMZDM.class, "ranking", new Object[]{SMZDM.RankType.好价品类榜_运动户外}, 10);
        env.addSource(source).setParallelism(1)
                .flatMap(new FlatMapFunction<List<Map<String, Object>>, Map<String, Object>>() {
                    @Override
                    public void flatMap(List<Map<String, Object>> maps, Collector<Map<String, Object>> collector) throws Exception {
                        for (Map<String, Object> map : maps) {
                            collector.collect(map);
                        }
                    }
                })
                .print()
        ;

        env.execute("smzdm 户外运动排行榜");
    }
}
