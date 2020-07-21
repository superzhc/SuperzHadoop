package com.github.superzhc.spark;

import com.github.superzhc.spark.udaf.AverageUdafDemo;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * 2020年07月13日 superz add
 */
public class SparkRegisterFunDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();

        SparkSession sparkSession = SparkSession.builder().appName("SparkSessionDemo").master("local").config(conf)
                .getOrCreate();

        /* 注册自定义聚合函数 */
        sparkSession.udf().register("averageudafdemo", new AverageUdafDemo());
    }
}
