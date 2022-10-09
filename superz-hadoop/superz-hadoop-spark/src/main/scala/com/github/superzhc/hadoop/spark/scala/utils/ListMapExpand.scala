package com.github.superzhc.hadoop.spark.scala.utils

class ListMapExpand[T](val self: java.util.List[java.util.Map[String, T]]) {
  def keys: Seq[String] = {
    import scala.collection.mutable.Set
    val set = Set[String]()

    import scala.collection.JavaConversions._
    for (map <- self) {
      for (key <- map.keys) {
        set.add(key)
      }
    }

    set.toSeq
  }
}

object ListMapExpand {
  implicit def toListMapExpand[T](lstMap: java.util.List[java.util.Map[String, T]]): ListMapExpand[T] = {
    new ListMapExpand(lstMap)
  }
}
