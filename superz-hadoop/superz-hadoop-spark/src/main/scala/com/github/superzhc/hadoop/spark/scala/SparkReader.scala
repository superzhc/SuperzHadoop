package com.github.superzhc.hadoop.spark.scala

import com.github.superzhc.data.stock.SinaStock
import org.apache.hudi.DataSourceWriteOptions.{OPERATION, PRECOMBINE_FIELD, RECORDKEY_FIELD, UPSERT_OPERATION_OPT_VAL}
import org.apache.hudi.config.HoodieWriteConfig.TBL_NAME
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkReader {
  def main(args: Array[String]): Unit = {
    // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
    System.setProperty("HADOOP_USER_NAME", "superz")

    val conf: SparkConf = new SparkConf()
      .setAppName("rdd demo")
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      // 扩展Spark SQL，使Spark SQL支持Hudi
      .set("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")

    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext
    // sc.setLogLevel("debug")

    val stocks = SinaStock.stocks()

    import spark.implicits._

    import scala.collection.JavaConversions._
    val rdd = sc.parallelize(stocks)
    //     .foreach(println)

    val df = rdd.map(row => (System.currentTimeMillis(), row.get("code"), row.get("open"), row.get("high"), row.get("low"), row.get("volume"), row.get("amount"), row.get("turnoverratio")))
      .toDF("ts", "c", "o", "h", "l", "v", "a", "t")

    df.write
      .mode(SaveMode.Overwrite)
      .format("hudi")
      //      .option("hoodie.insert.shuffle.parallelism", "2")
      //      .option("hoodie.upsert.shuffle.parallelism", "2")
      //      .option(TABLE_TYPE_OPT_KEY, "COPY_ON_WRITE")
      .option(RECORDKEY_FIELD.key, "c") // 主键字段
      .option(PRECOMBINE_FIELD.key, "ts") // 预合并字段
      // .option(PARTITIONPATH_FIELD.key, partitionField) // 分区字段
      .option(OPERATION.key(), UPSERT_OPERATION_OPT_VAL)
      //      // 下面的参数和同步hive元数据，查询hive有关
      //      .option(META_SYNC_ENABLED.key, true)
      //      .option(HIVE_USE_JDBC.key, false)
      //      .option(HIVE_DATABASE.key, databaseName)
      //      .option(HIVE_AUTO_CREATE_DATABASE.key, true)
      //      // 内部表，这里非必须，但是在用saveAsTable时则必须，因为0.9.0有bug，默认外部表
      //      .option(HIVE_CREATE_MANAGED_TABLE.key, true)
      //      .option(HIVE_TABLE.key, tableName)
      //      .option(HIVE_CREATE_MANAGED_TABLE.key, true)
      //      .option(HIVE_STYLE_PARTITIONING.key, true)
      //      .option(HIVE_PARTITION_FIELDS.key, partitionField)
      //      .option(HIVE_PARTITION_EXTRACTOR_CLASS.key, classOf[MultiPartKeysValueExtractor].getName)
      //      // 为了SparkSQL更新用，0.9.0版本有bug，需要设置这个参数，最新版本已经修复，可以不设置这个参数
      //      // 详情查看PR：https://github.com/apache/hudi/pull/3745
      //      .option(DataSourceWriteOptions.HIVE_TABLE_SERDE_PROPERTIES.key, s"primaryKey=$primaryKey")
      .option(TBL_NAME.key(), "superz_test")
    // 将 core-site.xml,hdfs-site.xml 放到目录下
    //.save("hdfs://10.90.15.142:8020/user/superz/hudi")
      .save("/user/superz/hudi")

    spark.stop()
  }
}
