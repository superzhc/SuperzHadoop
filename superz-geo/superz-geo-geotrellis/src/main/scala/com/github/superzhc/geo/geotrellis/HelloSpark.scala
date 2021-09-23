package com.github.superzhc.geo.geotrellis

import org.apache.spark.SparkContext

/**
 * 将 GeoTrellis 和 Spark 结合使用
 *
 * @author superz
 * @create 2021/9/11 14:24
 */
object HelloSpark {
  def main(args: Array[String]): Unit = {
    val conf = new org.apache.spark.SparkConf()
    conf.setMaster("local[*]")
    /* 注意 implicit 关键字，因为 GeoTrellis 实现的读取的数据源都需要 Spark 上下文，若不设置这个关键字，需要用户主动调用这个参数，不方便 */
    implicit val sc:SparkContext = geotrellis.spark.util.SparkUtils.createSparkContext("Test console", conf)


  }

}
