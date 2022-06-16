package com.github.common.visualization.echart.example;

import com.github.superzhc.common.html.HtmlOutput;
import org.icepear.echarts.Graph;
import org.icepear.echarts.charts.graph.GraphEdgeItem;
import org.icepear.echarts.charts.graph.GraphEdgeLineStyle;
import org.icepear.echarts.charts.graph.GraphNodeItem;
import org.icepear.echarts.charts.graph.GraphSeries;
import org.icepear.echarts.components.series.SeriesLabel;
import org.icepear.echarts.components.series.SeriesLineLabel;
import org.icepear.echarts.render.Engine;

/**
 * @author superz
 * @create 2022/6/15 17:41
 **/
public class GraphBasicMain {
    public static void main(String[] args) {
        GraphNodeItem[] datas = new GraphNodeItem[4];
        for (int i = 0; i < 4; i++) {
            int x = i + 1;
            GraphNodeItem data = new GraphNodeItem()
                    .setName("Node " + x)
                    .setX(300 + (x / 2) * 150 + (x % 2) + 150)
                    .setY(100 + (x % 3 == 0 ? -1 : 1) * (x / 2) * 150 + (x % 2) + 150);
            datas[i] = data;
        }

        GraphEdgeItem[] links = new GraphEdgeItem[6];

        GraphEdgeItem link1 = new GraphEdgeItem()
                .setSource(0)
                .setTarget(1)
                .setSymbolSize(new Number[]{5, 20})
                .setLabel(new SeriesLineLabel().setShow(true))
                .setLineStyle(new GraphEdgeLineStyle().setWidth(5).setCurveness(0.2));
        links[0] = link1;

        for (int i = 1; i < 6; i++) {
            int source = i % 4;
            int target = (source + 1) % 4;

            GraphEdgeItem link2 = new GraphEdgeItem()
                    .setSource("Node " + source)
                    .setTarget("Node " + target);
            links[i] = link2;
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
                .setData(datas)
                .setLinks(links)
                .setLineStyle(new GraphEdgeLineStyle().setOpacity(0.9).setWidth(2).setCurveness(0));

        Graph graph = new Graph()
                .setTitle("Basic Graph")
                .addSeries(series);


        Engine engine = new Engine();
        // engine.render(System.getProperty("user.dir") + "/testoutput/index.html", graph);
        String html= engine.renderHtml(graph);
        HtmlOutput.projectChart(html,"GraphDemo","echart");
    }
}
