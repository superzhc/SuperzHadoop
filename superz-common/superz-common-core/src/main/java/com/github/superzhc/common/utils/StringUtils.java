package com.github.superzhc.common.utils;

/**
 * 2020年06月11日 superz add
 */
public class StringUtils
{
    public static boolean isBlank(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static boolean isNotBlank(String str) {
        return !(str == null || "".equals(str.trim()));
    }

    /**
     * 字符长度计算，直接使用str.length对中文计算的时候不够准确
     * @param value
     * @return
     */
    public static int length(String value) {
        int valueLength = 0;
        // String chinese = "[\u0391-\uFFE5]";//匹配中文字符的正则表达式： [\u4e00-\u9fa5]
        String doubleChar = "[^\\x00-\\xff]";// 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(doubleChar)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            }
            else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
