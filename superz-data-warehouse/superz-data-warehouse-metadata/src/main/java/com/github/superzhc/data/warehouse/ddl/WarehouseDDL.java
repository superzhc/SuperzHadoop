package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.format.PlaceholderResolver;
import com.github.superzhc.common.jackson.JsonUtils;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/4/15 17:28
 **/
public final class WarehouseDDL {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDL.class);
    private static final String DEFAULT_DDL_SCHEMA_PATH = "/ddl_table_schema.json";

    private static Pattern pattern = Pattern.compile("^([\\s\\S]+)\\[([0-9]+)\\]$");

    public static String convert(final JsonNode json, final WarehouseDDLDialect dialect) {
        // 校验json是否合法
        InputStream in = WarehouseDDL.class.getResourceAsStream(DEFAULT_DDL_SCHEMA_PATH);
        JsonSchema jsonSchema = JsonUtils.jsonSchema(in);
        Set<ValidationMessage> errors = jsonSchema.validate(json);
        if (errors.size() > 0) {
            throw new RuntimeException("元数据配置不符合规范！\n" + errors);
        }

        String sql = PlaceholderResolver.getResolver("${", "}")
                .resolveByRule(dialect.ddlTemplate(), new Function<String, String>() {
                    @Override
                    public String apply(String param) {
                        if (null == param) {
                            return "";
                        }

                        Object[] jsonPaths = paramSplit(param);
                        Object value = JsonUtils.objectValue(json, jsonPaths);
                        String dialectValue = dialect.convertParam(param, value);
                        return null == dialectValue ? param : dialectValue;
                    }
                });

        return sql;
    }

    private static Object[] paramSplit(String param) {
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
