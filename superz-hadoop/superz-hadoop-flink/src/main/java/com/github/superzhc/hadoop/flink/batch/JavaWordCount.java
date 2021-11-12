package com.github.superzhc.hadoop.flink.batch;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 在 Flink 1.12 版本中，可以使用 DataStream API 来处理有限流（例如文件），但需要注意的是，运行时并不“知道”作业的输入是有限的。
 * <p>
 * 为了优化在有限流情况下运行时的执行性能，新的 BATCH 执行模式，对于聚合操作，全部在内存中进行，且使用 sort-based shuffle（FLIP-140）和优化过的调度策略（请参见 Pipelined Region Scheduling 了解更多详细信息）。因此，DataStream API 中的 BATCH 执行模式已经非常接近 Flink 1.12 中 DataSet API 的性能。
 * <p>
 * 注意：尽管 DataSet API 尚未被弃用，但建议用户优先使用具有 BATCH 执行模式的 DataStream API 来开发新的批作业，并考虑迁移现有的 DataSet 作业。
 *
 * @author superz
 * @create 2021/11/12 11:39
 */
public class JavaWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 在 Flink 1.12 中，默认执行模式为 STREAMING，要将作业配置为以 BATCH 模式运行
         * 1. 可以在提交作业的时候，设置参数 execution.runtime-mode：`bin/flink run -Dexecution.runtime-mode=BATCH target/superz-hadoop-flink.jar`
         * 2. 通过编程的方式，如下所示：
         */
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        DataStream<String> text = env.readTextFile(JavaWordCount.class.getResource("/data/wordcount.txt").getPath());
        DataStream<Tuple2<String, Integer>> counts = text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] tokens = value.split("\\W+");
                        for (String token : tokens) {
                            if (token.length() > 0) {
                                out.collect(new Tuple2<>(token, 1));
                            }
                        }
                    }
                })//
                .keyBy(0)
                .sum(1)//
                ;

        counts.print();

        env.execute();
    }
}
