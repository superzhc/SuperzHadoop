package com.github.superzhc.fund.plot;

import com.github.superzhc.fund.data.index.IndexData;
import com.github.superzhc.xchart.TableXChartTool;
import org.knowm.xchart.*;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/31 14:00
 **/
public class PlotMain {
    public static void main(String[] args) {
        String index = "000905.SH";
        Table table = Table.create();

//        table = IndexData.history(index);
//        table = table.where(table.dateColumn("date").isAfter(LocalDate.now().minusMonths(3)));

//        table=IndexData.industry(index);

//        table = IndexData.industry(index);

        table=IndexData.pe(index);

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());

//        XYChart chart = TimeSeries.lineChart(table, "date", "CLOSE", "OPEN", "HIGH", "LOW");

//        CategoryChart chart2 = TimeSeries.barChart(table, "date", "amplitude", "quote_change");

//        OHLCChart chart2 = TableXChartTool.ohlcChart(table, "date", "OPEN", "HIGH", "LOW", "CLOSE","volume");

//        PieChart chart2 = TableXChartTool.pie(table, "csiTypeL1En", "preciseWeight");

//        XYChart chart2=TableXChartTool.scatter4date(table,"date","pe");

        CategoryChart chart2=TableXChartTool.histogram(table,"pe");

        //new SwingWrapper<>(Arrays.asList(chart, chart2), 2, 1).displayChartMatrix();
        new SwingWrapper<>(chart2).displayChart();
    }
}
