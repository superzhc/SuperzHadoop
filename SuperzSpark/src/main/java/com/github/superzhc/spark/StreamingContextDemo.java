package com.github.superzhc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * StreamingContext是流计算功能的主要入口。
 * StreamingContext会在底层创建出SparkContext，用来处理数据。
 * 其构造函数还接收用来指定多长时间处理一次新数据的批次间隔（batch interval）作为输入
 * 注意：一个StreamingContext只能启动一次，所以只有在配置好所有DStream以及所需要输出操作之后才能启动
 * 2020年07月21日 superz add
 */
public class StreamingContextDemo
{
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf();
        conf.setMaster("local[2]");// 至少需要两个线程，存在一个receiver
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));// 指定1s的批处理大小

        /* 设定好要进行的计算，系统收到数据时计算就会开始 */
        // doing something

        /*
         * 必须显式调用StreamingContext的start()方法
         * 这样，Spark-Streaming就会开始把Spark作业不断交给下面的SparkContext去调度执行
         */
        // 启动流计算环境StreamingContext并等待它“完成”
        jssc.start();
        // 等待作业完成
        jssc.awaitTermination();
    }
}
