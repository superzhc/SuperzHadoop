package com.github.superzhc.common.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author superz
 * @create 2022/6/15 20:59
 */
public class PathUtils {

    /**
     * 获取项目路径
     *
     * @return
     */
    public static String project() {
        try {
            // return new File("").getCanonicalPath();
            return System.getProperty("user.dir");
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(PathUtils.class.getResource("/").getPath());
    }
}
