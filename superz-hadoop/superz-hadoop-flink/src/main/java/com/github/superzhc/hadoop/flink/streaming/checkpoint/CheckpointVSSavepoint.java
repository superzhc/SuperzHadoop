package com.github.superzhc.hadoop.flink.streaming.checkpoint;

/**
 * @author superz
 * @create 2021/10/28 13:57
 */
public class CheckpointVSSavepoint {
    /**
     * Flink Checkpoint 是一种容错恢复机制。这种机制保证了实时程序运行时，即使突然遇到异常也能够进行自我恢复。Checkpoint 对于用户层面，是透明的，用户会感觉程序一直在运行。Flink Checkpoint 是 Flink 自身的系统行为，用户无法对其进行交互，用户可以在程序启动之前，设置好实时程序 Checkpoint 相关参数，当程序启动之后，剩下的就全交给 Flink 自行管理。当然在某些情况，比如 Flink On Yarn 模式，某个 Container 发生 OOM 异常，这种情况程序直接变成失败状态，此时 Flink 程序虽然开启 Checkpoint 也无法恢复，因为程序已经变成失败状态，所以此时可以借助外部参与启动程序，比如外部程序检测到实时任务失败时，从新对实时任务进行拉起。
     * Flink Savepoint 你可以把它当做在某个时间点程序状态全局镜像，以后程序在进行升级，或者修改并发度等情况，还能从保存的状态位继续启动恢复。Flink Savepoint 一般存储在 HDFS 上面，它需要用户主动进行触发。如果是用户自定义开发的实时程序，比如使用DataStream进行开发，建议为每个算子定义一个 uid，这样我们在修改作业时，即使导致程序拓扑图改变，由于相关算子 uid 没有变，那么这些算子还能够继续使用之前的状态，如果用户没有定义 uid ， Flink 会为每个算子自动生成 uid，如果用户修改了程序，可能导致之前的状态程序不能再进行复用。
     *
     * 对比：
     * 1. Checkpoint 是自动容错机制；Savepoint 程序全局状态镜像
     * 2. Checkpoint 是程序自动容错，快速恢复；Savepoint是程序修改后继续从状态恢复，程序升级等
     * 3. Checkpoint 是 Flink 系统行为；Savepoint是用户触发
     * 4. Checkpoint 默认程序删除，可以设置 CheckpointConfig 中的参数进行保留；Savepoint会一直保存，除非用户删除
     */
}
