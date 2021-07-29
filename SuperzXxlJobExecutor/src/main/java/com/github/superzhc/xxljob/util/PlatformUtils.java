package com.github.superzhc.xxljob.util;

/**
 * @author superz
 * @create 2021/7/28 12:15
 */
public class PlatformUtils {
    private PlatformUtils() {
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }
}
