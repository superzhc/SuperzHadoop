package com.github.superzhc.hadoop.flink.streaming.connector.customsink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author superz
 * @create 2021/10/25 15:12
 */
public class JavaRichSink extends RichSinkFunction<String> {
    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(String value, Context context) throws Exception {
        super.invoke(value, context);
    }
}
