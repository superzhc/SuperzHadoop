package com.github.superzhc.hadoop.flink.log;

/**
 * Flink 日志
 * @author superz
 * @create 2022/3/17 10:26
 **/
public class FlinkLog {
    /**
     * Flink 自带的默认日志配置文件
     *
     * - log4j-cli.properties：由 Flink 命令行客户端使用（例如 flink run）（不包括在集群上执行的代码）。这个文件是我们使用 flink run 提交任务时，任务提交到集群前打印的日志所需的配置。
     * - log4j-session.properties：Flink 命令行客户端在启动 YARN 或 Kubernetes session 时使用（yarn-session.sh，kubernetes-session.sh）。
     * - log4j.properties：作为 JobManager/TaskManager 日志配置使用（standalone 和 YARN 两种模式下皆使用）
     *
     * 使用 flink run 提交任务时，会自动去 $FLINK_HOME 下的 conf 目录下寻找 log.properties 的文件作为 jobmanager 和 taskmanager 的日志配置
     */
}
