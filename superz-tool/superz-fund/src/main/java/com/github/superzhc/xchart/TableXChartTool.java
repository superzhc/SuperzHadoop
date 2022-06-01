package com.github.superzhc.xchart;

import com.github.superzhc.common.xchart.XChartFunctions;
import com.github.superzhc.tablesaw.functions.DateFunctions;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/6/1 10:26
 **/
public class TableXChartTool {
    private static final Integer WIDTH = 1500;
    private static final Integer HEIGHT = 800;

    public static OHLCChart ohlcChart(Table table, String dateName, String openName, String highName, String lowName, String closeName) {
        OHLCChart chart = new OHLCChartBuilder().width(WIDTH).height(HEIGHT).title(table.name()).build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setToolTipsEnabled(true);

        double[] dateDatas = DateFunctions.convert2Long(table.dateColumn(dateName)).asDoubleArray();
        double[] openDatas = table.nCol(openName).asDoubleArray();
        double[] highDatas = table.nCol(highName).asDoubleArray();
        double[] lowDatas = table.nCol(lowName).asDoubleArray();
        double[] closeDatas = table.nCol(closeName).asDoubleArray();

        chart.addSeries("Open-High-Low-Close", dateDatas, openDatas, highDatas, lowDatas, closeDatas);

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);

        return chart;
    }

    // 价格线
    public static OHLCChart ohlcChart(Table table, String dateName, String openName, String highName, String lowName, String closeName, String volume) {
        OHLCChart chart = new OHLCChartBuilder().width(WIDTH).height(HEIGHT).title(table.name()).build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setToolTipsEnabled(true);

        double[] dateDatas = DateFunctions.convert2Long(table.dateColumn(dateName)).asDoubleArray();
        double[] openDatas = table.nCol(openName).asDoubleArray();
        double[] highDatas = table.nCol(highName).asDoubleArray();
        double[] lowDatas = table.nCol(lowName).asDoubleArray();
        double[] closeDatas = table.nCol(closeName).asDoubleArray();
        long[] volumeDatas = table.intColumn(volume).asLongColumn().asLongArray();

        chart.addSeries("Open-High-Low-Close", dateDatas, openDatas, highDatas, lowDatas, closeDatas, volumeDatas);

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);

        return chart;
    }

    public static XYChart lineChart(Table table, String dateName, String... yNames) {
        XYChart chart = new XYChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(table.name());
        chart.setXAxisTitle(dateName);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setCursorEnabled(true);
        chart.getStyler().setYAxisDecimalPattern("#,###.##");

        DateColumn dateColumn = table.dateColumn(dateName);
        double[] dateDatas = DateFunctions.convert2Long(dateColumn).asDoubleArray();
        for (String yName : yNames) {
            NumericColumn numericColumn = table.nCol(yName);
            XYSeries series = chart.addSeries(yName, dateDatas, numericColumn.asDoubleArray());
            series.setMarker(SeriesMarkers.NONE);
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);
        return chart;
    }

    public static CategoryChart barChart(Table table, String dateName, String... yNames) {
        CategoryChart chart = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).build();
        chart.setTitle(table.name());
        chart.setXAxisTitle(dateName);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setYAxisDecimalPattern("#,###.##");

        DateColumn dateColumn = table.dateColumn(dateName);
        double[] dateDatas = DateFunctions.convert2Long(dateColumn).asDoubleArray();

        for (String yName : yNames) {
            NumericColumn numericColumn = table.nCol(yName);
            chart.addSeries(yName, dateDatas, numericColumn.asDoubleArray());
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(XChartFunctions.timestamp2date);
        return chart;
    }
}
