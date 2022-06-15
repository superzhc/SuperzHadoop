package com.github.superzhc.common.visualization.xchart;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * @author superz
 * @create 2022/5/31 11:28
 **/
public class XChartUtils {
    public static XYSeries addSeries(XYChart chart, String seriesName, LocalDate[] xData, double[] yData) {
        double[] xData2 = new double[xData.length];
        for (int i = 0, len = xData.length; i < len; i++) {
            xData2[i] = xData[i].atStartOfDay(ZoneOffset.ofHours(+8)).toInstant().toEpochMilli();
        }
        return chart.addSeries(seriesName, xData2, yData);
    }

    public static XYSeries addSeries(XYChart chart, String seriesName, long[] xData, double[] yData) {
        double[] xData2 = new double[xData.length];
        for (int i = 0, len = xData.length; i < len; i++) {
            xData2[i] = xData[i];
        }
        return chart.addSeries(seriesName, xData2, yData);
    }
}
