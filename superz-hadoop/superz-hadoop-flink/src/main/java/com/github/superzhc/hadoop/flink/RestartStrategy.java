package com.github.superzhc.hadoop.flink;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.concurrent.TimeUnit;

/**
 * 重启策略
 * Flink 支持不同的重启策略，以便在故障发生时控制作业如何重启。集群在启动时会伴随一个默认的重启策略，在没有定义具体重启策略时会使用默认策略。
 * <p>
 * 常用的策略类型：
 * 1. 固定延时（Fixed delay）
 * 2. 失败率（Failure rate）
 * 3. 无重启（No restart）
 * <p>
 * 优先级：
 * 代码中动态指定 > `flink-conf.yaml` 全局配置
 * <p>
 * 注意事项：
 * 1. 如果没有启用 checkpointing，则使用无重启 (no restart) 策略。如果启用了 checkpointing，但没有配置重启策略，则使用固定间隔 (fixed-delay) 策略
 *
 * @author superz
 * @create 2021/10/9 15:21
 */
public class RestartStrategy {

    /**
     * 固定延时重启策略
     * <p>
     * `flink-conf.yaml` 全局配置：
     * restart-strategy: fixed-delay
     * restart-strategy.fixed-delay.attempts: 3
     * restart-strategy.fixed-delay.delay: 10 s
     */
    public static class FixedDelayRestartStrategy {
        public void setRestartStrategy(StreamExecutionEnvironment env) {
            env.setRestartStrategy(
                    /* 尝试重启 3 次，重试的间隔是 10s */
                    RestartStrategies.fixedDelayRestart(
                            3,// 尝试重启的次数
                            Time.of(10, TimeUnit.SECONDS) // 间隔
                    ));
        }
    }

    /**
     * 失败率重启策略
     * 失败率重启策略在Job失败后会重启，但是超过失败率后，Job会最终被认定失败。在两个连续的重启尝试之间，重启策略会等待一个固定的时间。
     * <p>
     * `flink-conf.yaml` 全局配置：
     * restart-strategy: failure-rate
     * restart-strategy.failure-rate.max-failures-per-interval: 3
     * restart-strategy.failure-rate.failure-rate-interval: 5 min
     * restart-strategy.failure-rate.delay: 10 s
     */
    public static class FailureRateRestartStrategy {
        public void setRestartStrategy(StreamExecutionEnvironment env) {
            env.setRestartStrategy(
                    /* 5 分钟内若失败了 3 次则认为该 job 失败，重试间隔为 10s */
                    RestartStrategies.failureRateRestart(
                            3,//一个时间段内的最大失败次数
                            Time.of(5, TimeUnit.MINUTES), // 衡量失败次数的是时间段
                            Time.of(10, TimeUnit.SECONDS) // 间隔
                    ));
        }
    }

    /**
     * 无重启策略
     * <p>
     * `flink-conf.yaml` 全局配置：
     * restart-strategy: none
     */
    public static class NoRestartStrategy {
        public void setRestartStrategy(StreamExecutionEnvironment env) {
            env.setRestartStrategy(RestartStrategies.noRestart());
        }
    }
}
