package com.github.superzhc.common.utils;

/**
 * 驼峰命名
 *
 * @author superz
 * @create 2022/7/1 17:16
 **/
public class CamelCaseUtils {
    public static String underscore2camelCase(String str) {
        StringBuilder sb = new StringBuilder();

        boolean b = false;
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (c == '_') {
                b = true;
            } else {
                if (b) {
                    sb.append(Character.toUpperCase(c));
                    b = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String camelCase2underscore(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i == 0) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append('_').append(Character.toLowerCase(c));
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String str1 = "abc_cdef_ggg_hh_ii";
        System.out.println(str1);
        System.out.println(underscore2camelCase(str1));
        String str2 = "abcDeFGHijklmnopqR_gT";
        System.out.println(str2);
        System.out.println(camelCase2underscore(str2));
    }
}
