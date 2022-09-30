package com.github.superzhc.hadoop.flink2.streaming.operator.map;

import org.apache.flink.api.common.functions.MapFunction;

import java.util.Map;

/**
 * @author superz
 * @create 2022/9/30 14:46
 **/
public class DropKeyFunction<T> implements MapFunction<Map<String, T>, Map<String, T>> {
    private String[] keys;

    public DropKeyFunction(String... keys) {
        this.keys = keys;
    }

    @Override
    public Map<String, T> map(Map<String, T> value) throws Exception {
        if (null == value) {
            return null;
        }

        if (null != keys) {
            for (String key : keys) {
                value.remove(key);
            }
        }
        return value;
    }
}
