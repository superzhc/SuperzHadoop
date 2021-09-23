package com.github.superzhc.geo.geotrellis

import geotrellis.raster.io.geotiff.{MultibandGeoTiff, SinglebandGeoTiff}
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.vector.Extent

/**
 * TIFF 数据读取
 *
 * @author superz
 * @create 2021/9/7 11:15
 */
object GeoTiffReaderMain {
  val path = GeoTrellisConfig.root + "/Helsinki_masked_p188r018_7t20020529_z34__LV-FIN.tif"

  def main(args: Array[String]): Unit = {
    readMultiband()
  }

  /**
   * 读取单波段的栅格
   * 如果强行用GeoTiffReader.readSingleband(path)方法去读取一个多波段影像，则最后的返回结果是一个单波段影像，且其中的数据为原始影像中的第一个波段
   */
  def readSingleband(): Unit = {
    val geoTiff: SinglebandGeoTiff = GeoTiffReader.readSingleband(path)
    println(geoTiff)
  }

  /**
   * 读取多波段的栅格
   */
  def readMultiband() = {
    val geoTiff: MultibandGeoTiff = GeoTiffReader.readMultiband(path)
    /* 获取栅格数据的相关属性 */
    // 坐标系
    println(geoTiff.crs)
    // 波段数
    println(geoTiff.tile.bandCount)
    println(geoTiff)
  }

  /**
   * 通过流读取的tif，单个小文件可以完整的读取，大文件则Java虚拟机会内存溢出崩溃，大文件和小文件都有一个共同之处，所选的区域越小读取的速度也快，所以对栅格读取区域的范围要严格的控制，避免程序崩溃。
   *
   * @return
   */
  def ioReader() = {
    //指定区域，注：原始区域的大小
    val e: Extent = Extent(698592.0, 6656859.0, 735300.0, 6697870.5)

    //单波段读取
    SinglebandGeoTiff(path, e)
    GeoTiffReader.readSingleband(path, e)

    //多波段读取
    MultibandGeoTiff(path, e)
    GeoTiffReader.readMultiband(path, e)

    //单波段读取
    SinglebandGeoTiff(path, Some(e))
    GeoTiffReader.readSingleband(path, Some(e))

    //多波段读取
    MultibandGeoTiff(path, Some(e))
    GeoTiffReader.readMultiband(path, Some(e))

    println()
  }

  /**
   * 裁剪读取
   */
  def cropReader(): Unit = {
    // 指定区域
    val e: Extent = Extent(698592.0, 6656859.0, 735300.0, 6697870.5)

    //单波段读取裁剪
    SinglebandGeoTiff.streaming(path).crop(e)
    println(SinglebandGeoTiff.streaming(path).crop(e))
    GeoTiffReader.readSingleband(path, true).crop(e)

    //多波段读取裁剪
    MultibandGeoTiff.streaming(path).crop(e)
    GeoTiffReader.readMultiband(path, true).crop(e)
  }
}
