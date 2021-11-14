package com.github.superzhc.hadoop.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

/**
 * RDD
 * 弹性分布式数据集
 * <p>
 * RDD 本质上是一个只读分区的记录的集合
 *
 * @author superz
 * @create 2021/11/12 9:45
 */
public class JavaRDDMain {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("superz").setMaster("local[1]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
    }

    /**
     * RDD 创建：
     * 1. 读取外部数据源（或内存中的集合）进行创建；
     * 2. 转化操作后生成新的 RDD
     */

    public <T> JavaRDD<T> parallelize(JavaSparkContext jsc, List<T> data) {
        return parallelize(jsc, data, jsc.defaultParallelism());
    }

    /**
     * 将列表转换成数据源
     *
     * @param jsc：SparkContext 上下文
     * @param data
     * @param numSlices：切片数
     * @param <T>
     * @return
     */
    public <T> JavaRDD<T> parallelize(JavaSparkContext jsc, List<T> data, Integer numSlices) {
        return jsc.parallelize(data, numSlices);
    }

    public JavaRDD<String> textFile(JavaSparkContext jsc, String path) {
        return textFile(jsc, path, jsc.defaultMinPartitions());
    }

    /**
     * 读取文本文件
     *
     * @param jsc：SparkContext上下文
     * @param path：文件的URI，支持多种多数据源，HDFS、Amazon S3、本地文件等
     * @param minPartitions
     * @return
     */
    public JavaRDD<String> textFile(JavaSparkContext jsc, String path, Integer minPartitions) {
        return jsc.textFile(path, minPartitions);
    }
}
