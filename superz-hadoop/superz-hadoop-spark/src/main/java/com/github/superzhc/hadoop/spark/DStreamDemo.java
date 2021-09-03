package com.github.superzhc.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.bouncycastle.util.Strings;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 2020年07月21日 superz add
 */
public class DStreamDemo
{
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf();
        conf.setAppName("superz").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(3));

        JavaDStream<String> jds = jssc.socketTextStream("localhost", 8090);
        jds.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            @Override
            public void call(JavaRDD<String> stringJavaRDD) throws Exception {
                System.out.println("遍历RDD，start...");
                stringJavaRDD.foreach(d-> System.out.println(d));
                System.out.println("遍历RDD，stop!");
            }
        });
//        JavaDStream<String> words = jds.flatMap(new FlatMapFunction<String, String>()
//        {
//            @Override
//            public Iterator<String> call(String s) throws Exception {
//                String[] ss = s.split(" ");
//                return Arrays.asList(ss).iterator();
//            }
//        });
//        JavaPairDStream<String, Integer> pairs = words.mapToPair(d -> new Tuple2(d, 1));
//        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);
//        wordCounts.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
