package com.github.superzhc.hadoop.spark.java.rdd;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.BiCiDo;
import com.github.superzhc.data.news.MoFish;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

//        List<Map<String,Object>> data= MoFish.taobaoAll();
//        System.out.println(MapUtils.print(data));

//        JavaRDD<Map<String,String>> rdd= jsc.parallelize(data);
//        List<Map<String,String>> lst=rdd.filter(row->row.get("Title").contains("家用")).collect();
//        System.out.println(JsonUtils.format(lst));

        JavaRDD<String> rdd = jsc.textFile("file:\\G:\\data\\200W快手数据pao   36.txt");
        System.out.println("数据量：" + rdd.count());
        rdd.foreach(d-> System.out.println(d));

//        JavaRDD<List<String>> rdd2 = rdd.map(line -> {
//                    String str = line.split(",", -1)[1];
//                    if (null == str || str.length() == 0) {
//                        return str;
//                    }
//
//                    return str.substring(1, str.length() - 1);
//                })
//                .map(d -> {
//                    List<String> lst = new ArrayList<>();
//                    if (null == d || d.trim().length() == 0) {
//                        return lst;
//                    }
//
//                    String[] arr = d.split("\\|");
//                    lst.add(String.valueOf(arr.length));
//                    for (String s : arr) {
//                        lst.add(s);
//                    }
//
//                    return lst;
//                })
//                .filter(d -> "6".equals(d.get(0)));
//
////        rdd2 = rdd2.map(d -> {
////                    String s1 = d.get(1).trim();
////                    String s2 = d.get(2).trim();
////                    if ("?".startsWith(s1)) {
////                        s1 = s1.substring(1);
////                    }
////                    if ("\\?".startsWith(s2)) {
////                        s2 = s2.substring(1);
////                    }
////                    return Arrays.asList(s1, s2);
////                })
////                .filter(d -> !d.get(0).matches("1[3456789]\\d{9}"))
////        ;
//        System.out.println("过滤后的数据量：" + rdd2.count());
//        rdd2.foreach(d -> System.out.println(d));


        jsc.stop();
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
