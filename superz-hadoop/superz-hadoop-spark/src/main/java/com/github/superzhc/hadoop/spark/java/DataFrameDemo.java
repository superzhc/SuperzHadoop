package com.github.superzhc.hadoop.spark.java;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020年07月13日 superz add
 */
public class DataFrameDemo {
    public static void main(String[] args) {
        /*创建SparkSession*/
        SparkConf conf = new SparkConf();
        SparkSession spark = SparkSession.builder()
                .appName("SparkSessionDemo")
                .master("local")
                .config(conf)
                .getOrCreate();

        List<Tuple3<String, Integer, Integer>> lst = new ArrayList<>();
        lst.add(new Tuple3<>("A", 2020, 1));
        lst.add(new Tuple3<>("A", 2020, 2));
        lst.add(new Tuple3<>("A", 2020, 3));
        lst.add(new Tuple3<>("A", 2020, 4));
        lst.add(new Tuple3<>("A", 2020, 5));
        lst.add(new Tuple3<>("A", 2020, 6));
        lst.add(new Tuple3<>("A", 2020, 7));
        lst.add(new Tuple3<>("B", 2020, 1));
        lst.add(new Tuple3<>("B", 2020, 2));
        lst.add(new Tuple3<>("B", 2020, 3));
        lst.add(new Tuple3<>("B", 2020, 4));
        lst.add(new Tuple3<>("B", 2020, 5));
        lst.add(new Tuple3<>("B", 2020, 6));
        lst.add(new Tuple3<>("B", 2020, 7));

        spark.createDataFrame(lst, Tuple3.class);

        spark.stop();
    }
}
