package com.github.superzhc.tablesaw.plot;

import tech.tablesaw.plotly.Plot;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 注意：此处的继承没太大的意义，只是想引用 protected 类型的静态属性而已
 *
 * @author superz
 * @create 2022/4/21 10:20
 **/
public class PlotUtils extends Plot {
    public static File index(String code, String name) {
        String fileName = String.format("index_%s_%s_%s.html", code, name, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return file(fileName);
    }

    public static File file(String fileName) {
        if (!fileName.endsWith(".html")) {
            fileName = fileName + ".html";
        }
        Path path = Paths.get(DEFAULT_OUTPUT_FOLDER, fileName);
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return path.toFile();
    }
}
