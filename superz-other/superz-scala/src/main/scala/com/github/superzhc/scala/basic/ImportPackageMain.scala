package com.github.superzhc.scala.basic

/**
 * 引入包
 *
 * @author superz
 * @create 2021/9/13 14:42
 */
object ImportPackageMain {
  /* 引入包的多个类 */
  // import java.util.{List, Map, Set}

  /* 重命名引入包 */

  import java.util.{List => JavaList, Map => JavaMap, Set}

  var javaList: JavaList[String] = _
  var javaMap: JavaMap[String, String] = _
  var set: Set[String] = _

  /* 引入指定包下的所有类 */

  import java.sql._

  var conn: Connection = _
}
