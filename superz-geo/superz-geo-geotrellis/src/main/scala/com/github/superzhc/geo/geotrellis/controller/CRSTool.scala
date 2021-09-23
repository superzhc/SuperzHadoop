package com.github.superzhc.geo.geotrellis.controller

import com.github.superzhc.geo.geotrellis.common.ResultT
import com.github.superzhc.geo.geotrellis.util.CoordinateTransformUtil
import geotrellis.proj4.{CRS, Transform}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

import scala.beans.BeanProperty

/**
 * @author superz
 * @create 2021/9/23 18:13
 */
@RestController
@RequestMapping(Array("/crs/tool"))
class CRSTool {

  case class LonLat(@BeanProperty x: Double, @BeanProperty y: Double)

  @GetMapping(Array("/transform"))
  def transform(srcCode: Int, destCode: Int, longitude: Double, latitude: Double) = {
    //    val src = CRS.fromEpsgCode(srcCode)
    //    val dest = CRS.fromEpsgCode(destCode)
    //    val transform = Transform(src, dest)
    //    val (newX, newY) = transform(x, y)
    //
    //    ResultT.success(LonLat(newX, newY))
    val arr: Array[Double] = CoordinateTransformUtil.wgs84togcj02(longitude, latitude)
    ResultT.success(LonLat(arr(0), arr(1)))
  }
}
