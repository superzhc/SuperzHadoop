package com.github.superzhc.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 2020年06月29日 superz add
 */
public class WordCount2
{
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf();
        conf.setAppName("superz").setMaster("local");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1 * 1000));
        JavaDStream<String> lines = ssc.socketTextStream("127.0.0.1", 8020);
        JavaDStream<String> words=lines.flatMap(new FlatMapFunction<String, String>()
        {

            @Override public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });
        JavaDStream<Tuple2<String,Integer>> words2=words.map(new Function<String, Tuple2<String,Integer>>()
        {
            @Override public Tuple2<String, Integer> call(String v1) throws Exception {
                return new Tuple2<>(v1,1);
            }
        });
        words2.print();
//        val words = lines.flatMap(d->d.split(" ")).map(word -> (word,1)).reduceByKey(_+_)
//        words.print();
        ssc.start();
        ssc.awaitTermination();
    }
}
