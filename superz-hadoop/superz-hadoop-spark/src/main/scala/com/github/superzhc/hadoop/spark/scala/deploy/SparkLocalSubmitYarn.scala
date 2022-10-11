package com.github.superzhc.hadoop.spark.scala.deploy

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 本地调试，将任务提交到 Yarn 上
 * TODO 未连上去
 */
object SparkLocalSubmitYarn {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\soft\\hadoop")
    //System.setProperty("user.name","root") // spark 读取系统用户设置的参数，可不用设置
    System.setProperty("HADOOP_USER_NAME", "root") // hdfs 相关操作读取的用户，必须设置，不然很容易报没有权限的问题

    val spark: SparkSession = SparkSession.builder()
      .appName("local submit yarn")
      .master("yarn")
      // .config("deploy-mode", "client")
      .config("deploy-mode", "cluster")
      // .config("spark.driver.host","")
      .config("spark.sql.warehouse.dir", "hdfs://log-platform01:8020/user/hive/warehouse")
      /* 将 "$SPARK_HOME"/jars 目录下的 jar 包上传到 hdfs 上 */
      .config("spark.yarn.jars", "hdfs://log-platform01:8020/spark/jars/*.jar")
      /* 用户 jar，多个包使用英文逗号进行分割 */
      // .config("spark.yarn.dist.jars","E:\\SuperzHadoop\\superz-hadoop\\superz-hadoop-spark\\target\\superz-hadoop-spark-0.2.1.jar")
      .enableHiveSupport()
      .getOrCreate()

    val df: DataFrame = spark.sql("select * from any_knew_hot_news")
    df.show()
  }
}
