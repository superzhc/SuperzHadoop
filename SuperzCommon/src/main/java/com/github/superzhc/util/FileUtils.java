package com.github.superzhc.util;

import java.io.File;

/**
 * 2020年04月30日 superz add
 */
public class FileUtils
{
    public static String name(String path) {
        String file = path;

        if (path.lastIndexOf(File.separator) > -1)
            path = path.substring(path.lastIndexOf(File.separator));

        if (file.lastIndexOf(".") > -1) {
            file = file.substring(1, file.lastIndexOf("."));
        }
        return file;
    }

    public static boolean deepDelete(File file) {
        if (file.isFile()) {
            // 先删除一次
            boolean success = file.delete();

            // 如果没删除掉，尝试 3 次
            int tryCount = 0;
            while (!success && tryCount++ < 3) {
                System.gc();
                success = file.delete();
            }
            return success;
        }
        else if (file.isDirectory()) {
            // 递归删除
            for (String s : file.list()) {
                boolean success = deepDelete(new File(file, s));
                if (!success)// 失败了则后面的文件不再进行操作
                    return false;
            }
            return file.delete();// 删除最后的目录
        }
        return true;
    }
}
