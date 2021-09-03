package com.github.superzhc.hadoop.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object wordcount_scala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("word count").setMaster("local")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val sc = spark.sparkContext
    val rdd = sc.textFile("D://data.txt")
    val wc = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    wc.cache()
    val max = wc.map(_._2).max()
    val maxWorldRdd = wc.filter(_._2 == max)

    import spark.implicits._
    val df = maxWorldRdd.toDF("world", "nums")
    df.write.parquet("test.parquet")
  }
}
