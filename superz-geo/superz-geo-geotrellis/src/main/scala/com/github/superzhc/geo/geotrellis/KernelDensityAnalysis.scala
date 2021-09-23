package com.github.superzhc.geo.geotrellis

import geotrellis.raster._
import geotrellis.raster.mapalgebra.focal.Kernel
import geotrellis.vector.{Extent, Feature, Point, PointFeature}
import scala.util.Random

/**
 * 核密度分析示例
 * <a href="http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iDesktop/Features/Analyst/Raster/KernelDensityAnalysis.html" >核密度分析</a>
 *
 * @author superz
 * @create 2021/9/7 14:09
 */
object KernelDensityAnalysis {
  def main(args: Array[String]): Unit = {
    val extent = Extent(-109, 37, -102, 41)

    def randomPointFeature(extent: Extent): PointFeature[Double] = {
      def randInRange(low: Double, high: Double): Double = {
        val x = Random.nextDouble
        low * (1 - x) + high * x
      }

      Feature(Point(randInRange(extent.xmin, extent.xmax), randInRange(extent.ymin, extent.ymax)), Random.nextInt % 16 + 16)
    }

    // 在指定区域中生成随机的点
    val pts = (for (i <- 1 to 1000) yield randomPointFeature(extent)).toList
    println(pts)

    // 使用核密度函数处理数据，并生成一个700*400分辨率的栅格地图
    val kernelWidth: Int = 9;
    val kern: Kernel = Kernel.gaussian(kernelWidth, 1.5, 25)
    val kde: Tile = pts.kernelDensity(kern, RasterExtent(extent, 700, 400))
    val colorMap = ColorMap(
      (0 to kde.findMinMax._2 by 4).toArray,
      ColorRamps.HeatmapBlueToYellowToRedSpectrum
    )

    // 输出地图，输出的格式可以选择为png图片或者tif栅格
    val root = "./superz-geo/superz-geo-geotrellis/data/"
    kde.renderPng(colorMap).write(root + /*"kernel_density_analysis.tif"*/ "kernel_density_analysis.png")
  }
}
