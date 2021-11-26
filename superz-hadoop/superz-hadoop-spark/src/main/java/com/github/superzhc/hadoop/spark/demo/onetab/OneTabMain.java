package com.github.superzhc.hadoop.spark.demo.onetab;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Iterator;

/**
 * @author superz
 * @create 2021/11/15 10:57
 */
public class OneTabMain {
    public static void main(String[] args) {
        //SparkContext sc=new SparkContext("local[1]","onetab");
        JavaSparkContext jsc = new JavaSparkContext("local[1]", "onetab");

        JavaRDD<String> rdd = jsc.textFile("D:\\work\\BigData-A-Question\\大数据文章采集\\OneTab\\2021年11月15日\\UNREAD.md", 1);

        // 数据的条数
        System.out.println("数据的条数：" + rdd.count());

        JavaRDD<String> rdd2 = rdd.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String v1) throws Exception {
                if (null == v1 || v1.length() == 0) {
                    return false;
                } else if (v1.startsWith("https://www.google.com/search")) {
                    return false;
                } else if (v1.contains("https://www.google.com.hk/search")) {
                    return false;
                } else if (v1.startsWith("chrome-extension://")) {
                    return false;
                }
                return true;
            }
        });
        System.out.println("清洗后的数据条数：" + rdd2.count());

        rdd2
                .groupBy(new Function<String, String>() {
                    @Override
                    public String call(String v1) throws Exception {
                        if (v1.startsWith("https://")) {
                            int index = v1.substring(8).indexOf("/");
                            return index == -1 ? v1 : v1.substring(0, 8 + index);
                        } else if (v1.startsWith("http://")) {
                            int index = v1.substring(7).indexOf("/");
                            return index == -1 ? v1 : v1.substring(0, 7 + index);
                        }
                        return null;
                    }
                })
                .foreach(new VoidFunction<Tuple2<String, Iterable<String>>>() {
                    @Override
                    public void call(Tuple2<String, Iterable<String>> stringIterableTuple2) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        Iterable<String> values = stringIterableTuple2._2;
                        for (String value : values) {
                            sb
//                                    .append(stringIterableTuple2._1)
//                                    .append(":")
                                    .append(";")
                                    .append(value);
                        }
                        System.out.println(sb.substring(1));
                    }
                });
        ;

//        rdd2.foreach(new VoidFunction<String>() {
//            @Override
//            public void call(String s) throws Exception {
//                System.out.println(s);
//            }
//        });
    }
}
