package com.github.superzhc.common.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * @author superz
 * @create 2022/6/16 10:26
 **/
public class HtmlOutput {
    private static final Logger log = LoggerFactory.getLogger(HtmlOutput.class);

    private static final String DEFAULT_OUTPUT_PATH = "testoutput";
    private static final String DEFAULT_CHART_PATH = "chart";

    public static String projectChart(String html, String fileName, String... dirs) {
        if (null != dirs) {
            String[] paths = new String[1 + dirs.length];
            paths[0] = DEFAULT_CHART_PATH;
            for (int i = 0, len = dirs.length; i < len; i++) {
                paths[i + 1] = dirs[i];
            }
            return write2project(html, paths, fileName);
        } else {
            return write2Project(html, fileName, DEFAULT_CHART_PATH);
        }
    }

    public static String project(String html, String fileName, String... dirs) {
        if (null != dirs) {
            String[] paths = new String[1 + dirs.length];
            paths[0] = DEFAULT_OUTPUT_PATH;
            for (int i = 0, len = dirs.length; i < len; i++) {
                paths[i + 1] = dirs[i];
            }
            return write2project(html, paths, fileName);
        } else {
            return write2Project(html, fileName, DEFAULT_OUTPUT_PATH);
        }
    }

    public static String write2Project(String html, String fileName, String... dirs) {
        return write2project(html, dirs, fileName);
    }

    private static String write2project(String html, String[] dirs, String fileName) {
        String currentProjectPath = System.getProperty("user.dir");
        String pathAppendDirOrFileNameTemp = "%s/%s";
        String path = currentProjectPath;
        for (String dir : dirs) {
            if (dir.startsWith("/") || dir.startsWith("\\")) {
                dir = dir.substring(1);
            }

            if (dir.endsWith("/") || dir.endsWith("\\")) {
                dir = dir.substring(0, dir.length() - 1);
            }

            path = String.format(pathAppendDirOrFileNameTemp, path, dir);
        }

        return write(html, String.format(pathAppendDirOrFileNameTemp, path, fileName));
    }

    public static String write(String html, String path, String fileName) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length() - 1);
        }
        return write(html, String.format("%s/%s", path, fileName));
    }

    public static String write(String html, String filePath) {
        String fileName;
        if (filePath.endsWith("/") || filePath.endsWith("\\")) {
            fileName = String.format("%s/%s.html", filePath, UUID.randomUUID().toString());
        } else if (!filePath.endsWith(".html")) {
            fileName = String.format("%s.html", filePath);
        } else {
            fileName = filePath;
        }

        File file = new File(fileName);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            log.debug("写入路径：{}", fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(html);
            writer.close();
            return fileName;
        } catch (IOException e) {
            log.error("Write Html failed. Path error.", e);
            return null;
        }
    }


}
