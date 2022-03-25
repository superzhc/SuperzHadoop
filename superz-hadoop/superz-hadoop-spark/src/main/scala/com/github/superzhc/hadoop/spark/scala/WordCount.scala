package com.github.superzhc.hadoop.spark.scala

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("word count").setMaster("local")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val sc = spark.sparkContext
    // 设置Spark程序运行时的日志等级
    sc.setLogLevel("warn")
    val rdd = sc.textFile("D://data.txt")
    val wc = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    wc.cache()
    val max = wc.map(_._2).max()
    val maxWorldRdd = wc.filter(_._2 == max)

    import spark.implicits._
    val df = maxWorldRdd.toDF("world", "nums")
    df.write.parquet("test.parquet")
    spark.stop()
  }
}
