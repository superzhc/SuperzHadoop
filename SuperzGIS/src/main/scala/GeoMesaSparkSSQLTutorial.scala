import org.apache.spark.sql.SparkSession

/**
 * Spark-SQL 需要导入如下包：
 * 1. geomesa-spark-sql_2.11
 *
 * @author superz
 * @create 2021/7/19 19:35
 */
object GeoMesaSparkSSQLTutorial {
  def main(args: Array[String]): Unit = {
    val dsParams = Map(
      "hbase.zookeepers" -> "namenode:2181,datanode1:2181,datanode1:2181"
      , "hbase.catalog" -> "cloud4control"
      , "hbase.coprocessor.url" -> "hdfs://datanode1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|"
    )

    // Create SparkSession
    val sparkSession = SparkSession.builder()
      .appName("testSpark")
      .config("spark.sql.crossJoin.enabled", "true")
      .master("local[*]")
      .getOrCreate()

    // Create DataFrame using the "geomesa" format
    val dataFrame = sparkSession.read
      .format("geomesa")
      .options(dsParams)
      .option("geomesa.feature", "vehicle.tpms")
      .load()

    dataFrame.createOrReplaceTempView("vehicle_tpms")

    val sqlQuery = "select * from vehicle_tpms where timestamp > '1990-01-01T00:00:00.000Z'"
    val resultDataFrame = sparkSession.sql(sqlQuery)

    resultDataFrame.show
  }
}
