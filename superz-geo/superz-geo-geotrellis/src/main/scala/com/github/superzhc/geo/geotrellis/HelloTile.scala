package com.github.superzhc.geo.geotrellis

import com.github.superzhc.geo.geotrellis.util.DateUtils
import geotrellis.raster.histogram.Histogram
import geotrellis.raster.render.{ColorMap, ColorRamp, RGB}
import geotrellis.raster.{IntArrayTile, NODATA, Tile}

/**
 * @author superz
 * @create 2021/9/22 16:07
 */
object HelloTile {
  def main(args: Array[String]): Unit = {
    val nd = NODATA

    val input = Array[Int](
      nd, 7, 1, 1, 3, 5, 9, 8, 2,
      9, 1, 1, 2, 2, 2, 4, 3, 5,
      3, 8, 1, 3, 3, 3, 1, 2, 2,
      2, 4, 7, 1, nd, 1, 8, 4, 3)

    // 9 and 4 here specify columns and rows
    val tile = IntArrayTile(input, 9, 4)
    val histogram: Histogram[Int] = tile.histogram

    val colorRamp: ColorRamp =
      ColorRamp(
        RGB(r = 0xFF, b = 0x00, g = 0x00),
        RGB(r = 0x00, b = 0xFF, g = 0x00),
        RGB(r = 0x00, b = 0x00, g = 0xFF)
      )

    val colorMap = ColorMap.fromQuantileBreaks(histogram, colorRamp)

    // val coloredTile: Tile = tile.color(colorMap)
    tile.renderPng(colorMap).write(GeoTrellisConfig.root + s"${DateUtils.format()}_${HelloTile.getClass.getSimpleName}.png")

    /**
     * /zooom
     *      |-x
     *      |
     */
  }
}
