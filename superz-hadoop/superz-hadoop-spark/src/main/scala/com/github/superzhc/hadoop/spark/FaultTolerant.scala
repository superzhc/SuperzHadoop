package com.github.superzhc.hadoop.spark

/**
 * 容错原理
 *
 * Spark 框架层面的容错机制，主要分为三大方面（调度层、RDD 血统层、Checkpoint 层），在这三大层面中包括 Spark RDD 容错四大核心要点：
 * 1. Stage 输出失败，上层调度器 DAGScheduler 重试
 * 2. Spark 计算中，Task 内部任务失败，底层调度器重试
 * 3. RDD Lineage 血统中宽窄依赖计算
 * 4. Checkpoint 缓存
 *
 * @author superz
 * @create 2021/12/22 16:31
 */
object FaultTolerant {

}
