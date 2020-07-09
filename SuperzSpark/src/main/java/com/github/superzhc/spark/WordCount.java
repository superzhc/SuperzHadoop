package com.github.superzhc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 2020年07月02日 superz add
 */
public class WordCount
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("demo").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> rdd = sc.textFile(WordCount.class.getClassLoader().getResource("data/wordcount.txt").getPath());
        JavaPairRDD<String, Integer> rdd2 = rdd.flatMap(d -> Arrays.asList(d.split(" ")).iterator())
                .mapToPair(d -> new Tuple2<>(d, 1));
//                .mapToPair(new PairFunction<String, String, Integer>()
//                {
//                    @Override
//                    public Tuple2<String, Integer> call(String s) throws Exception {
//                        return new Tuple2<>(s, 1);
//                    }
//                });

        rdd2 = rdd2.reduceByKey((x, y) -> x + y);
        rdd2.foreach(d -> System.out.printf("(%s,%d)\n", d._1, d._2));
    }
}
