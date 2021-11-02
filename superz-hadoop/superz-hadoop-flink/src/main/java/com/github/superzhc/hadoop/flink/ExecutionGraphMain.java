package com.github.superzhc.hadoop.flink;

/**
 * ExecutionGraph
 * StreamGraph 和 JobGraph 都是在 Client 端生成的，JobGraph 相比于 StreamGraph 已经进行了一些优化，当 Client 端将 JobGraph 提交给 JobManager 时，JobManager 会根据 JobGraph 生成对应的 ExecutionGraph。
 *
 * TaskManager 最终会根据 ExecutionGraph 执行任务。
 *
 * @author superz
 * @create 2021/11/2 13:58
 */
public class ExecutionGraphMain {
}
