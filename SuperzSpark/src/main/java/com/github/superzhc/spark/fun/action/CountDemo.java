package com.github.superzhc.spark.fun.action;

import com.github.superzhc.spark.WordCount;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 2020年07月10日 superz add
 */
public class CountDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("demo").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> rdd = sc.textFile(WordCount.class.getClassLoader().getResource("data/wordcount.txt").getPath());

        // 统计数量
        long count = rdd.count();
        System.out.println(count);
    }
}
