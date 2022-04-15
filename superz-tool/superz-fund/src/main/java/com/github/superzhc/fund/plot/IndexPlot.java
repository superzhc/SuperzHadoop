package com.github.superzhc.fund.plot;

import com.github.superzhc.fund.akshare.CSIndex;
import com.github.superzhc.fund.akshare.Sina;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/4/14 14:02
 **/
public class IndexPlot {

    public static void csIndexHistory(String symbol) {
        Table table = CSIndex.indexHistory(symbol);
        if (!table.isEmpty()) {
            String indexName = table.row(0).getString("指数中文简称");

            Path path = Paths.get("testoutput",
                    String.format("index_%s_%s_%s.html", symbol, indexName, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            );
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            File file = path.toFile();

            Plot.show(
                    TimeSeriesPlot.create(String.format("%s[%s]", indexName, symbol), table, "日期", "收盘"),
                    file
            );
        }
    }

    public static void sinaIndexHistory(String indexName,String symbol) {
        Table table = Sina.indexHistory(symbol);
        if (!table.isEmpty()) {
            Path path = Paths.get("testoutput",
                    String.format("index_%s_%s_%s.html", symbol, indexName, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            );
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            File file = path.toFile();

            Plot.show(
                    TimeSeriesPlot.create(String.format("%s[%s]", indexName, symbol), table, "date", "close"),
                    file
            );
        }
    }

    public static void main(String[] args) {
        //csIndexHistory("399986");
        sinaIndexHistory("沪深300","sh000300");
    }
}
