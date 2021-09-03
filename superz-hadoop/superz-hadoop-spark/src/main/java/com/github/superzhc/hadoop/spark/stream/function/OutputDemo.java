package com.github.superzhc.hadoop.spark.stream.function;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.dstream.DStream;

/**
 * 2020年07月22日 superz add
 */
public class OutputDemo
{
    /**
     * Spark-Streaming允许DStream的数据被输出到外部系统，如数据库或文件系统。
     * 由于输出操作实际上使transformation操作后的数据可以通过外部系统被使用，同时输出操作触发所有DStream的transformation操作的实际执行（类似于RDD操作）
     */

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaDStream<String> jds = jssc.socketTextStream("localhost", 8090);

        /* 在Driver中打印出DStream中数据的前10个元素 */
        jds.print();

        jds.foreachRDD(new VoidFunction2<JavaRDD<String>, Time>()
        {
            @Override
            public void call(JavaRDD<String> v1, Time v2) throws Exception {
                System.out.println(v2.toString());
            }
        });

        // Fixme：Java版本没有保存数据到文件的方法
        DStream<String> ds = jds.dstream();
        ds.saveAsTextFiles("", null);
    }
}
