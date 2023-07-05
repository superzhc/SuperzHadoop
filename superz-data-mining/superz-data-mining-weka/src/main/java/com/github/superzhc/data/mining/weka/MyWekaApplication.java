package com.github.superzhc.data.mining.weka;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * @author superz
 * @create 2023/7/6 0:52
 */
public class MyWekaApplication {
    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = ConverterUtils.DataSource.read("dataset.csv");
    }
}
