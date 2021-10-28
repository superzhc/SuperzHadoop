package com.github.superzhc.hadoop.flink.streaming.checkpoint;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 为了使 Flink 的状态具有良好的容错性，Flink 提供了检查点机制 (CheckPoints) 。
 *
 * 通过检查点机制，Flink 定期在数据流上生成 checkpoint barrier ，当某个算子收到 barrier 时，即会基于当前状态生成一份快照，然后再将该 barrier 传递到下游算子，下游算子接收到该 barrier 后，也基于当前状态生成一份快照，依次传递直至到最后的 Sink 算子上。
 * 当出现异常后，Flink 就可以根据最近的一次的快照数据将所有算子恢复到先前的状态。
 *
 * 默认情况下，Flink不会触发一次 Checkpoint 当系统有其他 Checkpoint 在进行时，也就是说 Checkpoint 默认的并发为 1。
 * @author superz
 * @create 2021/10/27 9:38
 */
public class CheckpointMain {
    /**
     * 针对 Flink DataStream 任务，程序需要经历从 StreamGraph -> JobGraph -> ExecutionGraph -> 物理执行图四个步骤，其中在 ExecutionGraph 构建时，会初始化 CheckpointCoordinator。
     *  ExecutionGraph 通过 ExecutionGraphBuilder.buildGraph 方法构建，在构建完时，会调用 ExecutionGraph 的 enableCheckpointing 方法创建 CheckpointCoordinator。
     *  CheckpoinCoordinator 是 Flink 任务 Checkpoint 的关键，针对每一个 Flink 任务，都会初始化一个 CheckpointCoordinator 类，来触发 Flink 任务 Checkpoint。
     */

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /* 默认情况下，检查点机制是关闭的，需要在程序中进行开启 */
        env.enableCheckpointing(1000);

        // 其他可选配置如下：

        /**
         * 设置语义
         * Flink Checkpoint 支持两种语义：Exactly Once 和 At least Once，默认的 Checkpoint 模式是 Exactly Once.
         * Exactly Once 和 At least Once 具体是针对 Flink 状态而言。
         *
         */
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        /**
         * 设置两个检查点之间的最小时间间隔
         * 当 Checkpoint 时间比设置的 Checkpoint 间隔时间要长时，可以设置 Checkpoint 间最小时间间隔 。这样在上次 Checkpoint 完成时，不会立马进行下一次 Checkpoint，而是会等待一个最小时间间隔，然后再进行该次 Checkpoint。否则，每次 Checkpoint 完成时，就会立马开始下一次 Checkpoint，系统会有很多资源消耗 Checkpoint。
         */
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);

        // 设置执行Checkpoint操作时的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 设置最大并发执行的检查点的数量
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

        /**
         * Checkpoint 保存的状态在程序取消时，默认会进行清除。
         *
         * Checkpoint 状态保留策略有两种:
         * 1. DELETE_ON_CANCELLATION 表示当程序取消时，删除 Checkpoint 存储文件。
         * 2. RETAIN_ON_CANCELLATION 表示当程序取消时，保存之前的 Checkpoint 存储文件。
         *
         * 用户可以结合业务情况，设置 Checkpoint 保留模式。
         */
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 如果有更近的保存点时，是否将作业回退到该检查点
        env.getCheckpointConfig().setPreferCheckpointForRecovery(true);

        /**
         * 默认情况下，如果设置了 Checkpoint 选项，则 Flink 只保留最近成功生成的 1 个Checkpoint，而当 Flink 程序失败时，可以从最近的这个 Checkpoint 来进行恢复。
         * 但是，如果希望保留多个 Checkpoint，并能够根据实际需要选择其中一个进行恢复，这样会更加灵活，比如，用户发现最近4个小时数据记录处理有问题，希望将整个状态还原到4小时之前。
         * Flink可以支持保留多个Checkpoint，需要在Flink的配置文件 conf/flink-conf.yaml 中，添加如下配置，指定最多需要保存 Checkpoint 的个数：
         *
         * state.checkpoints.num-retained: 20
         */


    }
}
