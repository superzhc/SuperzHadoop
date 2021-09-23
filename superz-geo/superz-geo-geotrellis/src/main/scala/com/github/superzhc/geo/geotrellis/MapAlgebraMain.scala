package com.github.superzhc.geo.geotrellis

import geotrellis.raster.{IntArrayTile, Tile}

/**
 * @author superz
 * @create 2021/9/13 19:23
 */
object MapAlgebraMain {
  def main(args: Array[String]): Unit = {
    val tile1:Tile = IntArrayTile(Array(1, 2, 3, 4), 2, 2)
    val tile2:Tile = IntArrayTile(Array(9, 8, 7, 6), 2, 2)

    var resultTile: Tile = null

    // import geotrellis.raster.mapalgebra.local.Implicits
    // If tile1 and tile2 are the same dimensions, we can combine them using local operations
    resultTile = tile1.localAdd(tile2)

    // There are operators for some local operations.
    // This is equivalent to the localAdd call above
    // 2021年9月13日 存在隐式转换么？？？
    // tile1 + tile2 //报异常，类型上面的问题

    // There is a local operation called "reclassify" in literature,
    // which transforms each value of the function.
    // We actually have a map method defined on Tile,
    // which serves this purpose.

    tile1.map { z => z + 1 } // Map over integer values.

    tile2.mapDouble { z => z + 1.1 } // Map over double values.

    tile1.dualMap({ z => z + 1 })({ z => z + 1.1 }) // Call either the integer value or double version, depending on cellType.

    // You can also combine values in a generic way with the combine funciton.
    // This is another local operation that is actually defined on Tile directly.
    tile1.combine(tile2) { (z1, z2) => z1 + z2 }
  }
}
