package com.github.superzhc.hadoop.spark.demo.moviedata

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * 数据预览
 *
 * 数据下载地址：https://grouplens.org/datasets/movielens/
 *
 * @author superz
 * @create 2021/12/23 9:52
 */
object MovieMain {
  val path: String = "D:\\downloads\\Chrome\\ml-25m\\"

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("movie main").setMaster("local[4]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // movieId,tagId,relevance
    val genomeScores: Array[String] = sc.textFile(path + "genome-scores.csv").take(20)
    println("genome-scores:\n" + genomeScores.mkString("\n"))

    // tagId,tag
    val genomeTags = sc.textFile(path + "genome-tags.csv").take(20)
    println("genome-tags:\n" + genomeTags.mkString("\n"))

    // movieId,imdbId,tmdbId
    val links = sc.textFile(path + "links.csv").take(20)
    println("links:\n" + links.mkString("\n"))

    // movieId,title,genres
    val moviesRdd = sc.textFile(path + "movies.csv")
      // zipWithIndex 是将每一行的内容和行号组成一个二元数组，第一个位置是内容，第二个位置是行号，从0开始
      .zipWithIndex().filter(_._2 >= 1)
      .map(d => {
        val arr = d._1.split(",")
        //(arr(0), arr(1), arr(2))
        (arr(0), (arr(1), arr(2)))
      })
    val movies = moviesRdd.take(20)
    println("movies:\n" + movies.mkString("\n"))

    // userId,movieId,rating,timestamp
    val ratingsRdd = sc.textFile(path + "ratings.csv")
      .zipWithIndex().filter(_._2 >= 1)
      .map(d => {
        val arr = d._1.split(",")
        //(arr(0), arr(1), arr(2), arr(3))
        (arr(1), (arr(0), arr(2), arr(3)))
      })
    val ratings = ratingsRdd.take(20)
    println("ratings:\n" + ratings.mkString("\n"))

    // userId,movieId,tag,timestamp
    val tags = sc.textFile(path + "tags.csv").take(20)
    println("tags:\n" + tags.mkString("\n"))

    // 注意 join 操作支持 PairRDDFunction 类型，即二元元组才可以
    // val movieAndRatings=ratingsRdd.join(moviesRdd).take(20)
    // println(movieAndRatings.mkString("\n"))

    spark.stop()
  }
}
