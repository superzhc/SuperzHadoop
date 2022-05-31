package com.github.superzhc.fund.plot;

import com.github.superzhc.fund.data.index.IndexData;
import com.github.superzhc.tablesaw.functions.DateFunctions;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/5/31 14:00
 **/
public class PlotMain {
    public static void main(String[] args) {
        String index = "000905.SH";
        Table table = Table.create();

        table = IndexData.history(index);

//        table=IndexData.industry(index);

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());

        XYChart chart = new XYChartBuilder()
                .width(1500)
                .height(800)
                .title("TEST")
                .theme(Styler.ChartTheme.XChart)
                .xAxisTitle("date")
                .yAxisTitle("y")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setZoomEnabled(true);
        chart.getStyler().setCursorEnabled(true);

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        chart.getStyler().setxAxisTickLabelsFormattingFunction(timestamp -> Instant.ofEpochMilli(timestamp.longValue()).atZone(ZoneOffset.ofHours(8)).toLocalDate().format(dtFormatter));

        double[] dates = DateFunctions.convert2Long(table.dateColumn("date")).asDoubleArray();
        XYSeries series = chart.addSeries("CLOSE", dates, table.doubleColumn("close").asDoubleArray());
        series.setMarker(SeriesMarkers.NONE);
        series.setLineColor(XChartSeriesColors.GREEN);
//        chart.addSeries("OPEN", dates, table.doubleColumn("open").asDoubleArray()).setMarker(SeriesMarkers.NONE);
//        chart.addSeries("HIGH", dates, table.doubleColumn("high").asDoubleArray());
//        chart.addSeries("LOW", dates, table.doubleColumn("low").asDoubleArray());


        new SwingWrapper<>(chart).displayChart();
    }
}
