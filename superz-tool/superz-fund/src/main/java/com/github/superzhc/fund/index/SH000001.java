package com.github.superzhc.fund.index;

import com.github.superzhc.fund.akshare.CSIndex;
import com.github.superzhc.fund.akshare.JiuCaiShuo;
import com.github.superzhc.fund.akshare.Sina;
import com.github.superzhc.fund.tablesaw.utils.PlotUtils;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.Histogram;
import tech.tablesaw.plotly.api.PiePlot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;

import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2022/4/21 9:18
 **/
public class SH000001 {
    private static final String CODE = "000001.SH";

    public Table history() {
//        String code=CODE.substring(0,6);
//        return CSIndex.indexHistory(code);
        return Sina.indexHistory(CODE);
    }

    public void historyPlot() {
        Table t = history();

        Layout layout = Layout.builder(CODE, "date", "y").build();

        List<String> columnNames = Arrays.asList("open", "high", "low", "close");

        ScatterTrace[] traces = new ScatterTrace[4];
        for (int i = 0; i < 4; i++) {
            traces[i] =
                    ScatterTrace.builder(t.dateColumn("date"), t.numberColumn(columnNames.get(i)))
                            .showLegend(true)
                            .name(columnNames.get(i))
                            .mode(ScatterTrace.Mode.LINE)
                            .build();
        }

        Figure figure = new Figure(layout, traces);

        Plot.show(
                figure,
                PlotUtils.index(CODE, "History")
        );
    }

    public Table component(){
        Table table=JiuCaiShuo.indexComponent(CODE);
        return table;
    }

    public void componentPlot(){
        Table table=component();

        Plot.show(
                PiePlot.create(CODE,table,"name","weight")
                ,PlotUtils.index(CODE, "Component")
        );
    }

    public static void main(String[] args) {
        SH000001 index = new SH000001();

        Table table = index.history();
//        Table table=index.component();
//        System.out.println(table.printAll());
//        System.out.println(table.shape());
        Plot.show(
                Histogram.create("",table,"volume")
        );

//        index.historyPlot();
//        index.componentPlot();
    }
}
