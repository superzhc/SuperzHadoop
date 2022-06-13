package com.github.superzhc.common.file.text;

import java.io.*;

/*
 * @author superz
 * @create 2022/3/9 17:21
 */
public final class TextWriter {
    public static boolean overwrite(String path, String content) {
        return write(path, content);
    }

    public static boolean append(String path, String content) {
        return write(path, true, content);
    }

    public static boolean write(String path, String content) {
        return write(path, false, content);
    }

    public static boolean write(String path, boolean append, String content) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, append))) {
            bufferedWriter.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean overwrite(String path, byte[] bytes) {
        return write(path, bytes);
    }

    public static boolean append(String path, byte[] bytes) {
        return write(path, true, bytes);
    }

    public static boolean write(String path, byte[] bytes) {
        return write(path, false, bytes);
    }

    public static boolean write(String path, boolean append, byte[] bytes) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path, append))) {
            bufferedOutputStream.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
