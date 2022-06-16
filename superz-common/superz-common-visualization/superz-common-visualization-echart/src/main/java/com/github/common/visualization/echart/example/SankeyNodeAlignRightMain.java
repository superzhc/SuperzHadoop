package com.github.common.visualization.echart.example;

import org.icepear.echarts.Sankey;
import org.icepear.echarts.charts.sankey.*;
import org.icepear.echarts.components.tooltip.Tooltip;
import org.icepear.echarts.render.Engine;

/**
 * @author superz
 * @create 2022/6/16 13:55
 **/
public class SankeyNodeAlignRightMain {
    public static void main(String[] args) {
        SankeyNodeItem[] data = new SankeyNodeItem[10];
        for (int i = 0; i < 10; i++) {
            // 节点
            SankeyNodeItem item = new SankeyNodeItem()
                    // 注意：Name 不能重复
                    .setName("t" + i)
                    // 节点所属于的层级
                    // .setDepth(i % 3)
                    // .setValue(new String[]{"f1", "f2", "f3"})
                    ;
            data[i] = item;
        }

        SankeyEdgeItem[] links = new SankeyEdgeItem[6];
        for (int j = 0; j < 3; j++) {
            int x = j * 3;
            SankeyEdgeItem link1 = new SankeyEdgeItem()
                    .setSource("t" + x)
                    .setTarget("t" + (x + 1))
                    .setValue(100);

            SankeyEdgeItem link2 = new SankeyEdgeItem()
                    .setSource("t" + x)
                    .setTarget("t" + (x + 2))
                    .setValue(200);

//            SankeyEdgeItem link3 = new SankeyEdgeItem()
//                    .setSource("t" + x)
//                    .setTarget("t" + (x + 3));

            int y = j * 2;
            links[y] = link1;
            links[y + 1] = link2;
//            links[x + 2] = link3;
        }

        SankeySeries series = new SankeySeries()
                .setEmphasis(new SankeyEmphasis().setFocus("adjacency"))
                .setData(data)
                .setLinks(links)
                .setLineStyle(new SankeyEdgeStyle().setColor("source").setCurveness(0.5));

        Sankey sankey = new Sankey()
                .setTitle("Node Align Left")
                .setTooltip(new Tooltip().setTrigger("item").setTriggerOn("mousemove"))
                .addSeries(series);
        sankey.getOption().setAnimation(false);

        Engine engine = new Engine();
        engine.render(System.getProperty("user.dir") + "/chart/echart/sankey_node_align.html", sankey);
    }
}
