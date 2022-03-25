package com.github.superzhc.hadoop.spark.stream.function;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 窗口转换函数示例
 * 2020年07月22日 superz add
 */
public class WindowTransformDemo
{
    /**
     * 在Spark-Streaming中，数据处理是按批进行的，而数据采集是逐条进行的。因此在Spark-Streaming中会先设置好批处理间隔，当超过批处理间隔的时候就会把采集到的数据汇总起来成为一批数据交给系统来处理。
     * 对于窗口操作而言，在其窗口内部会有N个批处理数据，批处理数据的大小由窗口间隔（window duration）决定，而窗口间隔指的就是窗口的持续时间，在窗口操作中，只有窗口的长度满足了才会触发批数据的处理。
     * 除了窗口的长度，窗口操作还有另一个重要的参数就是滑动间隔（slide duration），它指的是经过多长时间窗口滑动一次形成新的窗口，滑动窗口默认情况下和批次间隔的相同，而窗口间隔一般设置的要比它们两个大。
     * 
     * 注意：滑动间隔和窗口间隔的大小一定得设置为批处理间隔的整数倍
     */

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaDStream<String> jds = jssc.socketTextStream("localhost", 8090);

        Duration windowDuration = Durations.minutes(1);
        Duration slideDuration = Durations.seconds(1);

        /* 返回一个基于源DStream的窗口批次计算后得到新的DStream */
        JavaDStream<String> wJds = jds.window(windowDuration, slideDuration);

        jds.countByValueAndWindow(windowDuration, slideDuration);

        jds.reduceByWindow(new Function2<String, String, String>()
        {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v1 + "@" + v2;
            }
        }, windowDuration, slideDuration);
    }
}
