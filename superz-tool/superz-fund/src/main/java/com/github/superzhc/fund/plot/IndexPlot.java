package com.github.superzhc.fund.plot;

import com.github.superzhc.financial.data.index.IndexData;
import com.github.superzhc.xchart.TableXChartTool;
import org.knowm.xchart.XYChart;
import tech.tablesaw.api.Table;

import static com.github.superzhc.financial.utils.IndexConstant.*;

/**
 * @author superz
 * @create 2022/6/2 13:57
 **/
public class IndexPlot {
    public static XYChart history(String symbol) {
        Table table = IndexData.history(symbol);
        XYChart chart = TableXChartTool.line4date(table, INDEX_TRADE_DATE, INDEX_TRADE_CLOSE);
        return chart;
    }
}
