package com.github.superzhc.spark.stream.function;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 普通转换操作
 * 2020年07月22日 superz add
 */
public class TransformDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaDStream<String> jds = jssc.socketTextStream("localhost", 8090);

        jds.map(new Function<String, String>()
        {
            @Override
            public String call(String v1) throws Exception {
                return null == v1 || v1.length() < 5 ? v1 : v1.substring(5);
            }
        });

        jds.flatMap(new FlatMapFunction<String, String>()
        {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        jds.filter(new Function<String, Boolean>()
        {
            @Override
            public Boolean call(String v1) throws Exception {
                return v1.length() > 5;
            }
        });

        jds.repartition(10);// 通过输入的参数来改变DStream的分区大小

        jds.union(jds);

        jds.count();// 对源DStream内部的所有的RDD的元素数量进行计数，返回一个内部的RDD只包含一个元素的DStream

        // 使用函数func（有两个参数并返回一个结果）将源DStream中每个RDD的元素进行聚 合操作,返回一个内部所包含的RDD只有一个元素的新DStream
        jds.reduce(new Function2<String, String, String>()
        {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v1 + "@" + v2;
            }
        });

        /* 计算DStream中每个RDD内的元素出现的频次并返回新的DStream[(K,Long)]，其中K是RDD中元素的类型，Long是元素出现的频次 */
        jds.countByValue();

        /**
         * 通过对源DStream的每RDD应用RDD-to-RDD函数返回一个新的DStream，这可以用来在DStream做任意RDD操作
         * 该transform操作（转换操作）及其类似的transformWith操作，允许在DStream上应用任意的RDD-to-RDD函数。它可以实现DStream API中未提供的操作
         */
        jds.transform(new Function<JavaRDD<String>, JavaRDD<String>>()
        {
            @Override
            public JavaRDD<String> call(JavaRDD<String> v1) throws Exception {
                return v1.filter(d -> d.length() > 5);
            }
        });
    }
}
