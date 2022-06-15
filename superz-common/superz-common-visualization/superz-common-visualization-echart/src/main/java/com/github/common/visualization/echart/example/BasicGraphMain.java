package com.github.common.visualization.echart.example;

import org.icepear.echarts.Graph;
import org.icepear.echarts.charts.graph.GraphNodeItem;
import org.icepear.echarts.charts.graph.GraphSeries;
import org.icepear.echarts.components.series.SeriesLabel;
import org.icepear.echarts.components.series.SeriesLineLabel;

/**
 * @author superz
 * @create 2022/6/15 17:41
 **/
public class BasicGraphMain {
    public static void main(String[] args) {
        GraphNodeItem[] datas = new GraphNodeItem[4];
        for (int i = 1; i <= 4; i++) {
            GraphNodeItem data = new GraphNodeItem()
                    .setName("Node " + i)
                    .setX(300 + (i / 2) * 150 + (i % 2) + 150)
                    .setY(100 + (i / 2) * 150 + (i % 2) + 150);
            datas[i] = data;
        }



        GraphSeries series = new GraphSeries()
                .setAnimationDurationUpdate(1500)
                .setAnimationEasingUpdate("quinticInOut")
                .setLayout("none")
                .setSymbolSize(50)
                .setRoam(true)
                .setLabel(new SeriesLabel().setShow(true))
                .setEdgeSymbol(new String[]{"circle", "arrow"})
                .setEdgeSymbolSize(new Number[]{4, 10})
                .setEdgeLabel(new SeriesLineLabel().setFontSize(20))
                .setData(datas);

        Graph graph = new Graph()
                .setTitle("Basic Graph")
                .addSeries(series);
    }
}
