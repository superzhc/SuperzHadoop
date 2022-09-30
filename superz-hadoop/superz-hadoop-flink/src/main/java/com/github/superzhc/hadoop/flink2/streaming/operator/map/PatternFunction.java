package com.github.superzhc.hadoop.flink2.streaming.operator.map;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/9/30 14:50
 **/
public class PatternFunction extends RichMapFunction<Map<String, String>, Map<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(PatternFunction.class);

    private static final boolean DEFAULT_IS_DELETE_ORIGIN_DATA = false;

    private String key;
    private String regex;
    private boolean isDeleteOriginData;
    private Pattern pattern;

    public PatternFunction(String key, String regex) {
        this(key, regex, DEFAULT_IS_DELETE_ORIGIN_DATA);
    }

    public PatternFunction(String key, String regex, boolean isDeleteOriginData) {
        this.key = key;
        this.regex = regex;
        this.isDeleteOriginData = isDeleteOriginData;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        pattern = Pattern.compile(regex);
    }

    @Override
    public Map<String, String> map(Map<String, String> value) throws Exception {
        String str = value.get(key);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            for (int i = 0, len = m.groupCount(); i <= len; i++) {
                value.put(String.format("%s_%d", key, i), m.group(i));
            }
        }

        if (isDeleteOriginData) {
            value.remove(key);
        }

        return value;
    }
}
