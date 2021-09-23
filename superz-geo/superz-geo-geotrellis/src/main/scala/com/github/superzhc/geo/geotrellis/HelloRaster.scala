package com.github.superzhc.geo.geotrellis

import com.github.superzhc.geo.geotrellis.util.DateUtils
import geotrellis.raster._
import geotrellis.raster.render.ascii._
import geotrellis.raster.mapalgebra.focal._
import geotrellis.raster.render.ColorRamp

/**
 * Raster Demo
 *
 * @author superz
 * @create 2021/9/6 14:35
 */
object HelloRaster {
  /**
   * map algebra 操作
   *
   * @param tile
   */
  @deprecated
  def mapAlgebra(tile: Tile): Unit = {
    // a 3x3 square neighborhood
    val focalNeighborhood = Square(1)
    // println(focalNeighborhood)
    // println("--------------------华丽的分割线---------------------")

    /**
     * 邻域平均法
     * 获取周边3*3的图像数据
     * */
    val meanTile = tile.focalMean(focalNeighborhood)
    // Should equal (1 + 7 + 9) / 3
    // val res1 = meanTile.getDouble(0, 0)
    // println(res1)
    for (row <- 0 to 3) {
      for (column <- 0 to 8) {
        printf("[%d,%d]:%f\n", (row + 1), (column + 1), meanTile.getDouble(column, row))

        /**
         * [1,3]:((7+1+1)+(1+1+2))/6=13/6=2.1666666666
         * [3,3]:((1+1+2)+(8+1+3)+(4+7+1))/3=28/9=3.111111111
         */
      }
    }
  }

  def main(args: Array[String]): Unit = {
    // 无数据
    val nd = NODATA
    // println(nd)
    // println("--------------------华丽的分割线---------------------")

    val input = Array[Int](
      nd, 7, 1, 1, 3, 5, 9, 8, 2,
      9, 1, 1, 2, 2, 2, 4, 3, 5,
      3, 8, 1, 3, 3, 3, 1, 2, 2,
      2, 4, 7, 1, nd, 1, 8, 4, 3)

    // 9 and 4 here specify columns and rows
    val iat = IntArrayTile(input, 9, 4)
    val colorRamp: ColorRamp = ColorRamps.GreenToRedOrange
    //iat.renderPng(colorRamp).write(GeoTrellisConfig.root + s"${DateUtils.format()}_hello_raster.png")

    val colorMap = ColorMap(Map(
      1 -> RGB(255, 255, 255)
      , 2 -> RGB(0, 255, 0)
      , 3 -> RGB(0, 0, 0)
      , 4 -> RGB(0, 0, 0)
      , 5 -> RGB(0, 0, 0)
      , 6 -> RGB(0, 0, 0)
      , 7 -> RGB(0, 0, 0)
      // , 8 -> RGB(0, 0, 0)
      , 9 -> RGB(0, 0, 0)
    ))
    iat.renderPng(colorMap).write(GeoTrellisConfig.root + s"${DateUtils.format()}_hello_raster2.png")

    // The renderAscii method is mostly useful when you're working with small tiles which can be taken in at a glance.
    // val res0 = iat.renderAscii(AsciiArtEncoder.Palette.STIPLED)
    // println(res0)
    // println("--------------------华丽的分割线---------------------")

    // mapAlgebra(iat)
  }
}
