package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author superz
 * @create 2021/10/25 14:54
 */
public class JavaNoParallelRichSource extends RichSourceFunction<Tuple2<Integer, String>> {
    /**
     * 初始化相关参数、连接
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void run(SourceContext<Tuple2<Integer, String>> ctx) throws Exception {

    }

    @Override
    public void cancel() {

    }
}
