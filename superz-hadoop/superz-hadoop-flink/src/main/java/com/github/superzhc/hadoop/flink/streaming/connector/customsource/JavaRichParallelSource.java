package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

/**
 * 自定义一个支持多并行度的Source
 * RichParallelSourceFunction会额外提供open和close方法
 * 如果在source中需要获取其他链接资源，那么可以在open方法中打开资源链接，在close中关闭资源链接
 * 2020年11月19日 superz add
 */
public class JavaRichParallelSource extends RichParallelSourceFunction<Long>
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

    /**
     * 这个方法只会在最开始的时候被调用一次
     * @param parameters
     * @throws Exception
     */
    @Override public void open(Configuration parameters) throws Exception {
        System.out.println("open......");
        super.open(parameters);
    }

    /**
     * 实现关闭链接的代码
     * @throws Exception
     */
    @Override public void close() throws Exception {
        super.close();
    }
}
