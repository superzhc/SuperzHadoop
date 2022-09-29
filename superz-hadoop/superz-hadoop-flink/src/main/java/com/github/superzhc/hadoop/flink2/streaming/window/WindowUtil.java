package com.github.superzhc.hadoop.flink2.streaming.window;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.StringUtils;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;

/**
 * @author superz
 * @create 2022/9/28 16:05
 **/
public class WindowUtil {
    public static void keyedWindow(DataStream<String> ds, String key) {
        final String[] keyPaths = StringUtils.escapeSplit(key, '.');
        KeyedStream<String, String> keyedStream = ds.keyBy(new KeySelector<String, String>() {
            @Override
            public String getKey(String value) throws Exception {
                return JsonUtils.string(value, keyPaths);
            }
        });
    }
}
