package com.github.superzhc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * SparkContext的示例
 * 2020年07月09日 superz add
 */
public class SparkContextDemo
{
    public static void main(String[] args) {
        // 初始化一个SparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("SparkContextDemo");// 设置Spark应用的名称
        /**
         * 设置Spark的模式
         */
        conf.setMaster("local");

        /**
         * 性能调优的一些参数
         */
        conf.set("spark.driver.memory", "2g");
        conf.set("spark.executor.memory", "4g");// 配10万条数据进行测试
        conf.set("spark.executor.cores", "2");
        conf.set("spark.executor.instances", "20");// 默认值是2个executor
        conf.set("spark.default.parallelism", "80");// 核数*executor实例数*(2~3)倍数

        JavaSparkContext jsc = new JavaSparkContext(conf);
    }
}
