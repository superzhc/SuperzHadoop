package com.github.superzhc.hadoop.spark.scala

import org.apache.spark.SparkConf

/**
 * 详细配置见：<a>https://spark.apache.org/docs/3.0.0/configuration.html</a>
 */
object SparkConfMain {
  val conf: SparkConf = new SparkConf()

  /* Spark Web UI 默认端口是 4040，配置 spark.ui.port 参数可进行修改 */
  conf.set("spark.ui.port", "4040")

  /**
   * 性能调优的一些参数
   */
  // conf.set("spark.driver.memory", "2g");
  // conf.set("spark.executor.memory", "4g");// 配10万条数据进行测试
  // conf.set("spark.executor.cores", "2");
  // conf.set("spark.executor.instances", "20");// 默认值是2个executor
  // conf.set("spark.default.parallelism", "80");// 核数*executor实例数*(2~3)倍数

  /**
   * Spark EventLog
   */
  //是否记录Spark任务的Event信息，用于应用程序在完成后重构webUI，若为false则EventLog相关参数无效，默认值为 false
  conf.set("spark.eventLog.enabled", "true")
  //保存Event相关信息的路径，可以是hdfs://开头的HDFS路径，也可以是file://开头的本地路径，都需要提前创建。不加文件系统前缀的话默认为HDFS目录，默认值 file:///tmp/spark-events
  conf.set("spark.eventLog.dir", "file:///tmp/spark-events")
  //是否压缩记录Spark事件，前提spark.eventLog.enabled为true，默认使用的是snappy，默认值 false
  conf.set("spark.eventLog.compress", "false")

  /**
   * Spark History Server
   */
  //History Server的默认访问端口，默认值 18080
  conf.set("spark.history.ui.port", "18080")
  //Event信息的存放路径，供History Server读取，默认值 file:/tmp/spark-events
  conf.set("spark.history.fs.logDirectory", "file:///tmp/spark-events")
  //日志文件解析刷新时间间隔，单位：秒。每次刷新会检查日志文件的更新情况，更新任务状态（incomplete -> complete）、根据应用程序数量限制清除超限的任务信息，默认值 10
  conf.set("spark.history.updateInterval", "10")
  //在History Server UI内存渲染中保留的最大应用程序数量，如果超过这个值，旧的应用程序信息将被从内存中清除。被清除的应用程序信息仍然可以从文件系统中重新加载，默认值 50
  conf.set("spark.history.retainedApplications", "50")
  //History Server UI列表页中保留的最大应用程序数量，如果超过这个值，旧的应用程序信息将被从UI列表页清除。被清除的应用程序信息仍然可以通过详情页的URL直接访问，前提是文件系统中对应的日志文件未过期，默认值 Int.MaxValue
  conf.set("spark.history.ui.maxApplications", String.valueOf(Integer.MAX_VALUE))
  //History Server是否从文件系统中清除过期的Event日志文件，若为false，则相关cleanner参数无效，默认值 false
  conf.set("spark.history.fs.cleaner.enabled", "false")
  //清除过期Event日志文件的频率，定时清理保留时长超过${spark.history.fs.cleaner.maxAge}的文件，默认值 1d
  conf.set("spark.history.fs.cleaner.interval", "1d")
  //Event日志文件保留时间，默认值 7d
  conf.set("spark.history.fs.cleaner.maxAge", "7d")
}
