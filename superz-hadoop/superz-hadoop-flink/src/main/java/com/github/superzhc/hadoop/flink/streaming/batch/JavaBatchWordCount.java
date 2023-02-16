package com.github.superzhc.hadoop.flink.streaming.batch;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author superz
 * @create 2023/2/10 17:47
 **/
public class JavaBatchWordCount {
    public static void main(String[] args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        /*
        * 设置执行模式：
        *
        * 执行模式可以通过 execute.runtime-mode 设置来配置。有三种可选的值：
        * - STREAMING: 经典 DataStream 执行模式（默认)
        * - BATCH: 在 DataStream API 上进行批量式执行
        * - AUTOMATIC: 让系统根据数据源的边界性来决定
        *
        * 示例，通过命令行配置执行模式
        * bin/flink run -Dexecution.runtime-mode=BATCH <jarFile>

        * Flink 官网建议：
        * 不建议用户在程序中设置运行模式，而是在提交应用程序时使用命令行进行设置。保持应用程序代码的免配置可以让程序更加灵活，因为同一个应用程序可能在任何执行模式下执行。
        * */
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    }
}
