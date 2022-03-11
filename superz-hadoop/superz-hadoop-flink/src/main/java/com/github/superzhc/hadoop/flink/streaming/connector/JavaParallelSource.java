package com.github.superzhc.hadoop.flink.streaming.connector;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

/**
 * 自定义实现一个支持多并行度的Source
 * 2020年11月19日 superz add
 */
public class JavaParallelSource implements ParallelSourceFunction<Long>
{
    private long count=1L;
    private boolean isRunning=true;

    @Override public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning) {
            ctx.collect(count);
            count++;
            Thread.sleep(1000);
        }
    }

    @Override public void cancel() {
        isRunning=false;
    }
}
