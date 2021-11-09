package com.github.superzhc.hadoop.flink;

/**
 * @author superz
 * @create 2021/11/9 13:54
 */
public class ExactlyOnce {
    /**
     * Flink 通过 异步快照机制 和 两阶段提交，实现了 “端到端的精确一次语义”
     *
     * Flink 采用基于 Checkpoint 机制，能够保证作业出现 fail-over 后可以从最新的快照进行恢复。
     */

    /**
     * 两阶段提交（2PC）
     * 1. 对于每个 checkpoint，sink 任务会启动一个事务，并将接下来所有接收的数据添加到事务里
     * 2. 然后将这些数据写入到外部 sink 系统，但不提交它们，这时只是“预提交”
     * 3. 当它收到 checkpoint 完成的通知时，它才正式提交事务，实现结果的真正写入
     */

    /**
     * Flink 中两阶段提交的实现方法被封装到了 TwoPhaseCommitSinkFunction 抽象类中，用户只需要实现其中的 beginTransaction、preCommit、commit、abort 四个方法就可以实现 “精确一次” 的处理语义
     * 1. beginTransaction，在开启事务之前，创建一个临时文件夹，然后把数据写入这个文件夹里面
     * 2. preCommit，在预提交阶段，将内存中缓存的数据写入文件并关闭；
     * 3. commit，在提交阶段，将预提交写入的临时文件放入目标目录下，这代表着最终的数据会有一些延迟；
     * 4. abort，在中止阶段，删除临时文件。
     */
}
