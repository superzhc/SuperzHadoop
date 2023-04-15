package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.base.Preconditions;
import com.github.superzhc.common.jackson.JsonUtils;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/4/15 17:33
 **/
public abstract class WarehouseDDLDialect implements Function<String, String> {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDLDialect.class);

    private static Pattern pattern = Pattern.compile("^([\\s\\S]+)\\[([0-9]+)\\]$");

    private JsonNode metadata;

    public WarehouseDDLDialect(JsonNode metadata) {
        this.metadata = metadata;
    }

    public abstract Set<ValidationMessage> validate();

    @Override
    public String apply(String param) {
        if (null == param) {
            return "";
        }

        Object[] paths = paramSplit(param);
        String value = JsonUtils.string(metadata, paths);
        return value;
    }

    private Object[] paramSplit(String param) {
        List<String> subStrs = new ArrayList<>();

        int len = param.length();
        boolean isEscape = false;
        StringBuilder subStr = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            // 未转义
            if (!isEscape) {
                if (c == '\\') {
                    isEscape = true;
                } else if (c == '.') {
                    subStrs.add(subStr.toString());
                    subStr.setLength(0);
                } else {
                    subStr.append(c);
                }
            } else {
                // 前一个字符是转义符，下一个字符还是转义符保持转义状态为真；其他状态下都为false
                if (c == '\\') {
                    // 将上一个非转义含义的字符添加到字串中
                    subStr.append('\\');
                    isEscape = true;
                } else if (c == '.') {
                    subStr.append(c);
                    isEscape = false;
                } else {
                    // 上一个字符是非转义字符的意义，因此也要加上
                    subStr.append('\\').append(c);
                    isEscape = false;
                }
            }
        }

        if (subStr.length() > 0) {
            subStrs.add(subStr.toString());
        }

        List<Object> paths = new ArrayList<>();
        // 对每个子字符串判断是否是数组
        for (String s : subStrs) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                paths.add(matcher.group(1));
                paths.add(Integer.valueOf(matcher.group(2)));
            } else {
                paths.add(s);
            }
        }
        return paths.toArray();
    }
}
