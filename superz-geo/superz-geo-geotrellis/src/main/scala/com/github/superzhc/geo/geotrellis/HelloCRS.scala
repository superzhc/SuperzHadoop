package com.github.superzhc.geo.geotrellis

import geotrellis.proj4.{CRS, Transform}

/**
 * 坐标参考系
 *
 * @author superz
 * @create 2021/9/13 16:03
 */
object HelloCRS {
  val WGS84 = CRS.fromEpsgCode(4326)

  val WGS84_2 = CRS.fromString("+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs")

  val EPSG3857=CRS.fromEpsgCode(3857)

  /**
   * 坐标转换
   *
   * @param src
   * @param dest
   * @param x
   * @param y
   * @return
   */
  def transform(src: CRS, dest: CRS, x: Double, y: Double): (Double, Double) = {
    val transform = Transform(src, dest)
    val (newX, newY) = transform(x, y)
    (newX, newY)
  }

  def main(args: Array[String]): Unit = {
    println(WGS84.toProj4String)

    /* 坐标参考系转换 */
     val transform = Transform(WGS84, EPSG3857)
     val (newX, newY) = transform(118.783,32.050)
    println(newX)
    println(newY)
  }
}
