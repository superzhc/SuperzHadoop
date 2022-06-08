package com.github.superzhc.xchart;

import com.github.superzhc.common.xchart.XChartFunctions;
import com.github.superzhc.tablesaw.functions.DateFunctions;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import tech.tablesaw.api.*;

import static tech.tablesaw.aggregate.AggregateFunctions.sum;

/**
 * 2022年6月8日 modify 修复 table.name() 为 null 的 bug
 *
 * @author superz
 * @create 2022/6/1 10:26
 **/
public class TableXChartTool {
    private static final Integer WIDTH = 1500;
    private static final Integer HEIGHT = 800;

    public static OHLCChart ohlc(Table table, String dateName, String openName, String highName, String lowName, String closeName) {
        OHLCChart chart = new OHLCChartBuilder().width(WIDTH).height(HEIGHT).title(null == table.name() ? "" : table.name()).build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setToolTipsEnabled(true);

        double[] dateDatas = DateFunctions.date2Long(table.dateColumn(dateName)).asDoubleArray();
        double[] openDatas = table.nCol(openName).asDoubleArray();
        double[] highDatas = table.nCol(highName).asDoubleArray();
        double[] lowDatas = table.nCol(lowName).asDoubleArray();
        double[] closeDatas = table.nCol(closeName).asDoubleArray();

        chart.addSeries("Open-High-Low-Close", dateDatas, openDatas, highDatas, lowDatas, closeDatas);

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);

        return chart;
    }

    // 价格线
    public static OHLCChart ohlc(Table table, String dateName, String openName, String highName, String lowName, String closeName, String volume) {
        OHLCChart chart = new OHLCChartBuilder().width(WIDTH).height(HEIGHT).title(null == table.name() ? "" : table.name()).build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setToolTipsEnabled(true);

        double[] dateDatas = DateFunctions.date2Long(table.dateColumn(dateName)).asDoubleArray();
        double[] openDatas = table.nCol(openName).asDoubleArray();
        double[] highDatas = table.nCol(highName).asDoubleArray();
        double[] lowDatas = table.nCol(lowName).asDoubleArray();
        double[] closeDatas = table.nCol(closeName).asDoubleArray();
        long[] volumeDatas = table.intColumn(volume).asLongColumn().asLongArray();

        chart.addSeries("Open-High-Low-Close", dateDatas, openDatas, highDatas, lowDatas, closeDatas, volumeDatas);

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);

        return chart;
    }

    public static XYChart line4date(Table table, String dateName, String... yNames) {
        XYChart chart = new XYChartBuilder().width(WIDTH).height(HEIGHT).build();
        if (null != table.name()) {
            chart.setTitle(table.name());
        }
        chart.setXAxisTitle(dateName);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setCursorEnabled(true);
        chart.getStyler().setYAxisDecimalPattern("#,###.##");

        DateColumn dateColumn = table.dateColumn(dateName);
        double[] dateDatas = DateFunctions.date2Long(dateColumn).asDoubleArray();
        for (String yName : yNames) {
            NumericColumn numericColumn = table.nCol(yName);
            XYSeries series = chart.addSeries(yName, dateDatas, numericColumn.asDoubleArray());
            series.setMarker(SeriesMarkers.NONE);
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);
        return chart;
    }

    public static CategoryChart bar4date(Table table, String dateName, String... yNames) {
        CategoryChart chart = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(null == table.name() ? "" : table.name());
        chart.setXAxisTitle(dateName);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setYAxisDecimalPattern("#,###.##");

        DateColumn dateColumn = table.dateColumn(dateName);
        double[] dateDatas = DateFunctions.date2Long(dateColumn).asDoubleArray();

        for (String yName : yNames) {
            NumericColumn numericColumn = table.nCol(yName);
            chart.addSeries(yName, dateDatas, numericColumn.asDoubleArray());
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);
        return chart;
    }

    public static XYChart scatter4date(Table table, String dateName, String... yNames) {
        XYChart chart = new XYChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(null == table.name() ? "" : table.name());
        chart.setXAxisTitle(dateName);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setMarkerSize(16);

        DateColumn dateColumn = table.dateColumn(dateName);
        double[] dateDatas = DateFunctions.date2Long(dateColumn).asDoubleArray();
        for (String yName : yNames) {
            NumericColumn numericColumn = table.nCol(yName);
            XYSeries series = chart.addSeries(yName, dateDatas, numericColumn.asDoubleArray());
            //series.setMarker(SeriesMarkers.NONE);
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);
        return chart;
    }

    // 直方图
    public static CategoryChart histogram(Table table, String dataName) {
        Histogram data = new Histogram(table.nCol(dataName).asList(), 100, 0, 100);

        CategoryChart chart = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(null == table.name() ? "" : table.name());
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setAvailableSpaceFill(.96);
        chart.getStyler().setOverlapped(true);

        chart.addSeries("Histogram", data.getxAxisData(), data.getyAxisData());
        return chart;
    }

    public static PieChart pie(Table table, String categoryName, String dataName) {
        PieChart chart = new PieChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(null == table.name() ? "" : table.name());

        Table t = table.summarize(dataName, sum).by(categoryName);
        for (Row row : t) {
            chart.addSeries(row.getString(0), row.getDouble(1));
        }

        return chart;
    }
}
