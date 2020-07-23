package com.github.superzhc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 2020年07月21日 superz add
 */
public class DStreamDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

        JavaDStream<String> jds = jssc.socketTextStream("localhost", 8090);
    }
}
