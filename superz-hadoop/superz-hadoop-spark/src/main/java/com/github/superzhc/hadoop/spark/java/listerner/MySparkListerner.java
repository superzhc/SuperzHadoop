package com.github.superzhc.hadoop.spark.java.listerner;

import org.apache.spark.scheduler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * spark 提供了一系列整个任务生命周期中各个阶段变化的事件监听机制，通过这一机制可以在任务的各个阶段做一些自定义的各种动作。
 * <p>
 * SparkListener便是这些阶段的事件监听接口类 通过实现这个类中的各种方法便可实现自定义的事件处理动作。
 *
 * @author superz
 * @create 2023/4/7 13:54
 **/
public class MySparkListerner extends SparkListener {
    private static final Logger LOG = LoggerFactory.getLogger(MySparkListerner.class);

    /**
     * 任务启动
     *
     * @param applicationStart
     */
    @Override
    public void onApplicationStart(SparkListenerApplicationStart applicationStart) {
        LOG.info("App[{}] 启动", applicationStart.appId());
    }

    /**
     * Job启动的事件
     *
     * @param jobStart
     */
    @Override
    public void onJobStart(SparkListenerJobStart jobStart) {
        LOG.info("Job[{}] 启动", jobStart.jobId());
    }

    @Override
    public void onJobEnd(SparkListenerJobEnd jobEnd) {
        LOG.info("Job[{}] 结束", jobEnd.jobId());
    }

    @Override
    public void onApplicationEnd(SparkListenerApplicationEnd applicationEnd) {
        LOG.info("App 结束");
    }
}
