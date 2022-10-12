package com.github.superzhc.hadoop.spark.scala.rdd

import com.github.superzhc.data.news.MoFish
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object ScalaRDDMain {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd demo").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext

    val data: java.util.List[java.util.Map[String, Object]] = MoFish.taobao3761()

    import scala.collection.JavaConverters._
    sc.parallelize(data.asScala)
      .foreach(d => println(d.asScala.mkString(";")))

    spark.stop()
  }
}
