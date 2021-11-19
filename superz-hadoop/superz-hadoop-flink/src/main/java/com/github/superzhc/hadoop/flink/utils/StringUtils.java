package com.github.superzhc.hadoop.flink.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2021/11/18 13:34
 */
public class StringUtils {
    // 回车、换行符、制表符匹配
    private static final Pattern STRING_BLANK = Pattern.compile(/*"\\s*|\\t|\\r|\\n"*/"\\t|\\r|\\n");

    public static String replaceBlank(String str) {
        if (null == str) {
            return null;
        }

        Matcher m = STRING_BLANK.matcher(str);
        return m.replaceAll("");
    }
}
