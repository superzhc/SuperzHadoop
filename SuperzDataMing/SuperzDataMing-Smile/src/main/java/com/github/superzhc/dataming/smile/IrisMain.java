package com.github.superzhc.dataming.smile;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;
import smile.plot.swing.Canvas;
import smile.plot.swing.ScatterPlot;

/**
 * @author superz
 * @create 2021/5/11 15:58
 */
public class IrisMain {
    public static void main(String[] args) throws Exception {
        DataFrame df = Read.arff("D:\\code\\SuperzHadoop\\SuperzDataMing\\data\\iris.arff");
//        System.out.println(df);

        // 随机森林
        RandomForest rf = RandomForest.fit(Formula.lhs("class"), df);
        System.out.printf("OOB error = %d", rf.metrics().error);

        // 图像展示
//        Canvas canvas=ScatterPlot.of(df, "sepallength", "sepalwidth", "class", '*').canvas();
//        canvas.setAxisLabels("sepallength", "sepalwidth");
//        canvas.window();
    }
}
