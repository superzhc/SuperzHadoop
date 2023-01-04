package com.github.superzhc.common.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author superz
 * @create 2021/9/17 16:12
 */
public class ZipUtils {
    public static void toZip(OutputStream out, String... fileNames) throws IOException {
        if (null == fileNames || fileNames.length == 0) {
            throw new RuntimeException("请添加需压缩的文件");
        }

        File[] files = new File[fileNames.length];
        for (int i = 0, len = fileNames.length; i < len; i++) {
            files[i] = new File(fileNames[i]);
        }
        toZip(out, files);
    }

    public static void toZip(OutputStream out, File... files) throws IOException {
        if (null == files || files.length == 0) {
            throw new RuntimeException("请添加需压缩的文件");
        }

        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File file : files) {
                if (file.exists()) {
                    compress(file, zos, file.getName());
                }
            }
        }
    }

    private static void compress(File source, ZipOutputStream zos, String name) throws IOException {
        byte[] buf = new byte[1024];
        if (source.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));

            int len;
            FileInputStream in = new FileInputStream(source);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] children = source.listFiles();
            if (null == children || children.length == 0) {
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (File child : children) {
                    compress(child, zos, name + File.separator + child.getName());
                }
            }
        }
    }
}