package com.github.superzhc.xxljob.util;

/**
 * @author superz
 * @create 2021/7/20 21:42
 */
public class OSUtil {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public enum OSPlatform {
        WINDOWS, MAC, LINUX
    }

    private OSUtil() {
    }

    public static OSPlatform getOS() {
        if (OS.startsWith("win")) {
            return OSPlatform.WINDOWS;
        } else if (OS.startsWith("mac")) {
            return OSPlatform.MAC;
        } else if (OS.startsWith("linux")) {
            return OSPlatform.LINUX;
        } else {
            throw new RuntimeException("当前平台[" + OS + "]尚未识别");
        }
    }
}
