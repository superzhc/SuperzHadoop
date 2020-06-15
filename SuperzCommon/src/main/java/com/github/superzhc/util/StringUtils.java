package com.github.superzhc.util;

/**
 * 2020年06月11日 superz add
 */
public class StringUtils
{
    public static boolean isBlank(String str) {
        return (str == null || "".equals(str.trim()));
    }
}
