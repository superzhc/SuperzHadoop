import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}
import org.geotools.data.Query
import org.geotools.filter.text.ecql.ECQL
import org.locationtech.geomesa.spark.GeoMesaSpark

/**
 * RDD 需要引入如下相关包
 * 1. geomesa-spark-core_2.11
 * 2. 对应存储源的包：geomesa-hbase-spark-runtime-hbase2_2.11
 *
 * @author superz
 * @create 2021/7/19 15:18
 */
object GeoMesaSparkRDDTutorial {
  def main(args: Array[String]): Unit = {
    val dsParams = Map(
      "hbase.zookeepers" -> "namenode:2181,datanode1:2181,datanode1:2181"
      , "hbase.catalog" -> "cloud4control"
      , "hbase.coprocessor.url" -> "hdfs://datanode1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|"
    )

    // set SparkContext
    val conf = new SparkConf().setMaster("local[*]").setAppName("testSpark")
    // 一定需要添加下面的配置才能本地访问远程的geomesa
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.kryo.registrator", "org.locationtech.geomesa.spark.GeoMesaSparkKryoRegistrator")
    val sc = SparkContext.getOrCreate(conf)


    import collection.JavaConverters._
    // create RDD with a geospatial query using GeoMesa functions
    val spatialRDDProvider = GeoMesaSpark(dsParams.asJava) // 此处跟官方文档存在差异，待排查 TODO
    val filter = ECQL.toFilter("timestamp AFTER 1990-01-01T00:00:00.000Z")
    val query = new Query("vehicle.tpms", filter)
    val resultRDD = spatialRDDProvider.rdd(new Configuration, sc, dsParams, query)

    val data = resultRDD.collect
    data.foreach(println)
  }
}
