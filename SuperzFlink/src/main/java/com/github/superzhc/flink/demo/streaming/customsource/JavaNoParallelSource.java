package com.github.superzhc.flink.demo.streaming.customsource;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * 自定义实现并行度为1的Source
 * 2020年11月19日 superz add
 */
public class JavaNoParallelSource implements SourceFunction<Long>
{
    private long count = 1L;

    private boolean isRunning = true;

    /**
     * 主要的方法，启动一个Source
     * 大部分情况下，都需要在这个run方法中实现一个循环
     * @param ctx
     * @throws Exception
     */
    @Override
    public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning) {
            ctx.collect(count);
            count++;
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
