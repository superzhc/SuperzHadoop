package com.github.superzhc.hadoop.flink.deploy;

/**
 * 部署模式
 *
 * @author superz
 * @create 2021/11/1 14:15
 */
public class DeployModeMain {
    /**
     * 1. 构建 StreamGraph [StreamGraphGenerator#generate]
     * 2. 获取 PipelineExecutorFactory [PipelineExecutorServiceLoader(DefaultExecutorServiceLoader)#getExecutorFactory]
     * 3. 获取 PipelineExecutor [PipelineExecutorFactory#getExecutor]
     * 4. 执行 execute，返回一个 CompletableFuture，可获取 JobExecutionResult
     */
}
