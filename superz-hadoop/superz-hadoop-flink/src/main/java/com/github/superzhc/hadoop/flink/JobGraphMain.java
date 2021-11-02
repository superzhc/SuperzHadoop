package com.github.superzhc.hadoop.flink;

/**
 * JobGraph
 * JobGraph 会在 StreamGraph 的基础上做相应的优化（主要是算子的 Chain 操作，Chain 在一起的算子将会在同一个 task 上运行，会极大减少 shuffer 的开销）
 *
 * @author superz
 * @create 2021/11/2 9:59
 */
public class JobGraphMain {
}
