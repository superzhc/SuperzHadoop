package com.github.superzhc.hadoop.spark.demo.moviedata

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * 电影流行度分析
 *
 * 需求：统计所有电影中平均得分最高（口碑最好）的电影以及电影粉丝或者观看人数最多的电影
 *
 * @author superz
 * @create 2021/12/23 10:57
 */
object MoviePopularityAnalyzer {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("movie popularity analyzer").setMaster("local[4]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // userId,movieId,rating,timestamp
    val ratingsRdd = sc.textFile(MovieMain.path + "ratings.csv")
      .zipWithIndex().filter(_._2 > 0)
      .map(d => {
        val arr = d._1.split(",")
        (arr(0), arr(1), arr(2), arr(3))
      })

    // 构建评分和次数，并根据movieId获取总的评分和观看次数
    val calRatingsRdd = ratingsRdd.map(d => (d._2, (d._3.toDouble, 1)))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

    // 根据评分排序，获取平均得分最高的电影
    println("所有电影中平均得分最高（口碑最好）的电影：")
    calRatingsRdd.map(d => (d._2._1 / d._2._2, d._1))
      .sortByKey(false)
      .take(10)
      .foreach(println)

    // 统计观看人数最多的电影
    println("所有电影中粉丝或者观看人数最多的电影")
    calRatingsRdd.map(d => (d._2._2, d._1))
      .sortByKey(false)
      .take(10)
      .foreach(println)

    spark.stop()
  }
}
