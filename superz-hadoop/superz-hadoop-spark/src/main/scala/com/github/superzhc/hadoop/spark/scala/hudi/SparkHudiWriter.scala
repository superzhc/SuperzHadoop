package com.github.superzhc.hadoop.spark.scala.hudi

//import org.apache.hudi.DataSourceWriteOptions._
//import org.apache.hudi.config.HoodieWriteConfig.TBL_NAME
//import org.apache.hudi.hive.NonPartitionedExtractor
//import org.apache.hudi.keygen.ComplexKeyGenerator
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkHudiWriter {
  def main(args: Array[String]): Unit = {
    // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
    System.setProperty("HADOOP_USER_NAME", "root")

    val conf: SparkConf = new SparkConf()
      .setAppName("hudi demo")
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //// 扩展Spark SQL，使Spark SQL支持Hudi[作用是什么？？]
    //.set("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")

    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // val stocks = SinaStock.stocks()

    //    import utils.ListMapExpand._
    //    println(stocks.keys)

    import spark.implicits._
    // val rdd = sc.parallelize(stocks)
    //     .foreach(println)

    val data = new Array[scala.collection.immutable.Map[String, Long]](100)
    for (i <- 0 until 100) {
      val map: Map[String, Long] = scala.collection.immutable.Map("ts" -> System.currentTimeMillis(), "id" -> i, "name" -> i * 1000)
      data(i) = map
    }

    val rdd = sc.parallelize(data)
    val df = rdd.map(row => (row.get("ts"), row.get("id"), row.get("name")))
      .toDF("ts", "id", "name")

    //    val df = rdd.map(row => (System.currentTimeMillis(), row.get("code"), row.get("open"), row.get("high"), row.get("low"), row.get("volume"), row.get("amount"), row.get("turnoverratio")))
    //      .toDF("ts", "c", "o", "h", "l", "v", "a", "t")
    //
    //    df.printSchema()
    //
    val tableName = "t_996121291814092801_hudi_test1"

    df.write
      .format("hudi")
      // 通常情况下使用 Append 模式，除非初始化创建使用 Overwrite 模式
      // mode(Overwrite) overwrites and recreates the table if it already exists
      .mode(SaveMode.Overwrite)
      //      .option("hoodie.insert.shuffle.parallelism", "2")
      //      .option("hoodie.upsert.shuffle.parallelism", "2")
//      .option(KEYGENERATOR_CLASS_NAME.key, classOf[ComplexKeyGenerator].getName)
//      .option(RECORDKEY_FIELD.key, "id") // 主键字段
//      // 更新数据时，如果存在两个具有相同主键的记录，则此列中的值将决定更新哪个记录。选择诸如时间戳记的列将确保选择具有最新时间戳记的记录。
//      .option(PRECOMBINE_FIELD.key, "ts") // 预合并字段
//      .option(PARTITIONPATH_FIELD.key, "") // 分区字段
//      .option(OPERATION.key(), BULK_INSERT_OPERATION_OPT_VAL) // 定义写操作类型。值可以为upsert，insert，bulk_insert和delete，默认值为upsert
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
      // 同步到hive
//      .option(HIVE_SYNC_MODE.key, "hms")
//      .option(HIVE_STYLE_PARTITIONING.key, true)
//      .option(HIVE_AUTO_CREATE_DATABASE.key, true)
//      .option(HIVE_DATABASE.key, "xgitbigdata")
//      .option(HIVE_TABLE.key, tableName)
//      .option(HIVE_PARTITION_FIELDS.key, "")
//      .option(HIVE_PARTITION_EXTRACTOR_CLASS.key, classOf[NonPartitionedExtractor].getName)
//      .option(TBL_NAME.key(), tableName)
      // 将 core-site.xml,hdfs-site.xml 放到目录下
      .save(s"/user/superz/hudi/$tableName")

    spark.stop()
  }
}
