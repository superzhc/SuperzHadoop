package com.github.superzhc.geo.geotrellis

import geotrellis.raster.{ColorMap, ColorRamp, ColorRamps}
import geotrellis.raster.io.geotiff.MultibandGeoTiff
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.render.RGB
import geotrellis.vector.Extent

/**
 * @author superz
 * @create 2021/9/7 18:12
 */
@deprecated
object Server extends App {
  val root = "./superz-geo/superz-geo-geotrellis/data/"
  val path = root + "Helsinki_masked_p188r018_7t20020529_z34__LV-FIN.tif"

  /* 多波段读取 */
  var geoTiff: MultibandGeoTiff = GeoTiffReader.readMultiband(path)

  //指定区域，注：原始区域的大小
  val e: Extent = Extent(698592.0, 6656859.0, 735300.0, 6697870.5)

  /* 数据裁剪 */
  geoTiff = geoTiff.crop(e)
}
