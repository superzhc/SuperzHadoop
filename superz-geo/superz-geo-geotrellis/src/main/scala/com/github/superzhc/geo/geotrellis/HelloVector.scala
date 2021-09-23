package com.github.superzhc.geo.geotrellis

import geotrellis.vector.{Geometry, LineString, Point, Polygon}

/**
 * 矢量数据操作
 *
 * @author superz
 * @create 2021/9/11 16:15
 */
object HelloVector {
  /* 点 */
  val p1 = Point(0, 0)
  val p2 = Point(0, 1)
  val p3 = Point(1, 1)
  val p4 = Point(1, 0)

  /* 线 */
  val lineString = LineString(p1, p2, p3)

  /* 面 */
  val polygon = Polygon(p1, p2, p3, p4, p1)

  // region Geometry 之间的关系

  /**
   * 相等
   * 几何形状拓扑上相等
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoEquals(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.equals(geometry2)
  }

  /**
   * 脱节
   * 几何形状不相交
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoDisjoint(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.disjoint(geometry2)
  }

  /**
   * 相交
   * 几何形状至少有一个共有点
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoIntersects(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.intersects(geometry2)
  }

  /**
   * 接触
   * 几何形状有至少一个公共的边界点，但是没有内部点
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoTouches(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.touches(geometry2)
  }

  /**
   * 交叉
   * 几何形状共享一些但不是所有的内部点
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoCrosses(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.crosses(geometry2)
  }

  def geoWithin(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.within(geometry2)
  }

  def geoContains(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.contains(geometry2)
  }

  /**
   * 重叠
   * 几何形状共享一部分但不是所有的公共点，而且相交处有他们自己相同的区域
   *
   * @param geometry1
   * @param geometry2
   * @return
   */
  def geoOverlaps(geometry1: Geometry, geometry2: Geometry) = {
    geometry1.overlaps(geometry2)
  }
  // endregion

  def main(args: Array[String]): Unit = {

    import geotrellis.vector.io.json.Implicits._
    println(p1.toGeoJson())

    println(geoEquals(p1, p2))
  }
}
