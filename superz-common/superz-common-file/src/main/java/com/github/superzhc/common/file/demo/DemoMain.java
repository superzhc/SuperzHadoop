package com.github.superzhc.common.file.demo;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @author superz
 * @create 2022/6/24 14:20
 **/
public class DemoMain {
    public static void main(String[] args) {
        String path = "E:\\downloads\\test.epub";

        try (ZipArchiveInputStream input = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(path), 1024))) {
            ZipArchiveEntry entry = null;
            while ((entry = input.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    System.out.println(entry.getName());
                } else {
                    if (!entry.getName().endsWith(".xhtml")) {
                        System.out.println(entry.getName());
                    } else {
                        final byte[] buffer = new byte[1024];
                        int read;
                        while ((read = input.read(buffer)) != -1) {
                            System.out.println(new String(buffer, 0, read));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
