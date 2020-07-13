package com.github.superzhc.spark.stream

import org.apache.spark.{SparkConf, SparkContext}

object Demo1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    //    sc.applicationId
  }
}
