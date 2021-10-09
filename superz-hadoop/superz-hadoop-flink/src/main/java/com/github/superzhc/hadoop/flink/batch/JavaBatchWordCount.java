package com.github.superzhc.hadoop.flink.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 2020年11月19日 superz add
 */
public class JavaBatchWordCount {
    public static void main(String[] args) throws Exception {
        // 获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 获取文件中的内容
        DataSource<String> text = env.readTextFile(JavaBatchWordCount.class.getResource("/data/wordcount.txt").getPath());
        DataSet<Tuple2<String, Integer>> counts = text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
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
                .groupBy(0)//
                .sum(1)//
                ;

        counts.print();

        /**
         * 2021年10月9日 superz [bug]
         * 报错信息：
         * Exception in thread "main" java.lang.RuntimeException: No new data sinks have been defined since the last execution. The last execution refers to the latest call to 'execute()', 'count()', 'collect()', or 'print()'.
         * 	at org.apache.flink.api.java.ExecutionEnvironment.createProgramPlan(ExecutionEnvironment.java:1165)
         * 	at org.apache.flink.api.java.ExecutionEnvironment.createProgramPlan(ExecutionEnvironment.java:1145)
         * 	at org.apache.flink.api.java.ExecutionEnvironment.executeAsync(ExecutionEnvironment.java:1041)
         * 	at org.apache.flink.api.java.ExecutionEnvironment.execute(ExecutionEnvironment.java:958)
         * 	at com.github.superzhc.hadoop.flink.batch.JavaBatchWordCount.main(JavaBatchWordCount.java:38)
         * 问题原因：
         * 从报错日志可看出，自上次执行以来，没有定义新的数据接收器。对于离线批处理的算子，如：“count()”、“collect()”或“print()”等既有sink功能，含有触发的功能。
         * 解决办法：
         * 去掉下面的执行即可
         */
        // env.execute("batch word count");
    }
}
