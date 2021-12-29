package com.github.superzhc.hadoop.spark.demo.company

import com.github.superzhc.common.jdbc.JdbcHelper
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.geotools.feature.{FeatureCollection, FeatureIterator}
import org.geotools.geojson.feature.FeatureJSON
import org.geotools.geojson.geom.GeometryJSON
import org.locationtech.jts.geom.{Coordinate, Geometry, GeometryFactory}
import org.opengis.feature.simple.{SimpleFeature, SimpleFeatureType}

import java.io.FileInputStream
import java.nio.file.{Files, Paths}

/**
 * @author superz
 * @create 2021/12/29 16:41
 */
//class CompanyAnalyzer {}

object CompanyAnalyzer {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("company analyzer job").setMaster("local")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("debug")

    val in = new FileInputStream("D:\\code\\SuperzHadoop\\superz-geo\\superz-geo-geotool\\src\\main\\resources\\100000\\440000.geoJson" /*classOf[CompanyAnalyzer].getResource("/100000/440000.geoJson").getPath*/)
    val featureJSON = new FeatureJSON(new GeometryJSON)
    val features: FeatureCollection[SimpleFeatureType, SimpleFeature] = featureJSON.readFeatureCollection(in).asInstanceOf[FeatureCollection[SimpleFeatureType, SimpleFeature]]

    val areaWithGeom = collection.mutable.Map[String, Geometry]()

    /* 通过查看 Geometry 的源码可知 Geometry 是可序列化的 */
    val sfIter: FeatureIterator[SimpleFeature] = features.features
    while (sfIter.hasNext) {
      val sf = sfIter.next()
      val geom: Geometry = sf.getDefaultGeometry.asInstanceOf[Geometry]
      areaWithGeom += (sf.getAttribute("name").toString -> geom)
    }
    //    var buffer = new Array[Byte](1024)
    //    val sb: StringBuilder = new StringBuilder
    //    while (in.read(buffer) != -1) {
    //      sb.append(buffer)
    //      buffer = new Array[Byte](1024)
    //    }
    //    in.close()
    //    val geojson = sb.toString()

    //    val geojson=new String(Files.readAllBytes(Paths.get("D:\\code\\SuperzHadoop\\superz-geo\\superz-geo-geotool\\src\\main\\resources\\100000\\440000.geoJson")))

    val geometryFactory = new GeometryFactory

    // 读取表数据
    val rdd: JdbcRDD[Array[Object]] = new JdbcRDD[Array[Object]](
      sc,
      () => new JdbcHelper(driver, url, username, password).getConnection,
      "select * from company_info where ? <= id and id < ?",
      1,
      8556800,
      80
      //,JdbcRDD.resultSetToObjectArray _
    )

    rdd.map(data => {
      try {
        val point = geometryFactory.createPoint(new Coordinate(data(10).toString.toDouble, data(9).toString.toDouble))

        //        val featureJSON = new FeatureJSON(new GeometryJSON)
        //        val features: FeatureCollection[SimpleFeatureType, SimpleFeature] = featureJSON.readFeatureCollection(geojson).asInstanceOf[FeatureCollection[SimpleFeatureType, SimpleFeature]]

        var area: String = "未知"

        //        val sfIter: FeatureIterator[SimpleFeature] = features.features
        //        var sfs: Seq[SimpleFeature] = Seq.empty[SimpleFeature]
        //        while (sfIter.hasNext) {
        //          val sf = sfIter.next()
        //          val geom: Geometry = sf.getDefaultGeometry.asInstanceOf[Geometry]
        //          if (geom.contains(point)) {
        //            area = sf.getAttribute("name").asInstanceOf[String]
        //          }
        //        }

        for ((k, v) <- areaWithGeom if v.contains(point)) {
          area = k
        }

        Tuple2(area, 1 /*data*/)

      } catch {
        case exception: Exception => {
          print(data.mkString(",") + ",")
          println(exception)
          ("未知", 1)
        }
      }

    })
      //.take(100)
      .countByKey()
      .foreach(println)

    spark.stop()
  }
}
