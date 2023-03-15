package com.github.superzhc.hadoop.flink.base;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Before;

/**
 * 执行模式可以通过 execute.runtime-mode 设置来配置。有三种可选的值：
 * <p>
 * STREAMING: 经典 DataStream 执行模式（默认)
 * BATCH: 在 DataStream API 上进行批量式执行
 * AUTOMATIC: 让系统根据数据源的边界性来决定
 *
 * @author superz
 * @create 2023/3/15 17:58
 **/
public class RuntimeModeTest {
    StreamExecutionEnvironment env;

    @Before
    public void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    public void streaming() {
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
    }

    public void batch() {
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    }

    public void automatic() {
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
    }
}
