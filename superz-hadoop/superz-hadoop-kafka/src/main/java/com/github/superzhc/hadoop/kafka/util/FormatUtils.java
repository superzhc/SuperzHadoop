package com.github.superzhc.hadoop.kafka.util;

public class FormatUtils {
    public static String multiLevelTab(Integer level) {
        return multiLevelDelimiter("\t", level);
    }

    public static String multiLevelDelimiter(String delimiter, Integer level) {
        StringBuilder sb = new StringBuilder();
        for (int i = level; i > 0; i--) {
            sb.append(delimiter);
        }
        return sb.toString();
    }
}
