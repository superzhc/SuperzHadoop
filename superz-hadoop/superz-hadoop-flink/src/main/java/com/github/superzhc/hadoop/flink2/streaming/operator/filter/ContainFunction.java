package com.github.superzhc.hadoop.flink2.streaming.operator.filter;

import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author superz
 * @create 2022/10/8 16:47
 **/
public class ContainFunction implements FilterFunction<Map<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(ContainFunction.class);

    private String key;
    private String subString;

    public ContainFunction(String key, String subString) {
        this.key = key;
        this.subString = subString;
    }

    @Override
    public boolean filter(Map<String, String> value) throws Exception {
        if (!value.containsKey(key)) {
            log.error("data[{}] don't containe key:{}", value, key);
            return false;
        }

        log.debug("data:{}", value);

        String v = value.get(key);
        return v.contains(subString);
    }
}
