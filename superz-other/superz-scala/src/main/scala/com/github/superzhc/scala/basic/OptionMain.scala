package com.github.superzhc.scala.basic

/**
 * Option(选项)类型用来表示一个值是可选的（有值或无值）
 *
 * @author superz
 * @create 2021/9/15 11:22
 */
object OptionMain {
  /*Option[T] 是一个类型为 T 的可选值的容器： 如果值存在， Option[T] 就是一个 Some[T] ，如果不存在， Option[T] 就是对象 None*/
  val map: Map[String, String] = Map("k1" -> "v1")

  /*Option 有两个子类别，一个是 Some，一个是 None，当回传 Some 的时候，代表这个函式成功地返回一个 String，可以通过 `get()` 这个函式拿到那个 String，如果返回的是 None，则代表没有值*/
  val value1: Option[String] = map.get("k1")
  val value2: Option[String] = map.get("k2")

  def main(args: Array[String]): Unit = {
    println(if (value1 == None) None else value1.get)
    println(value2)

    /*模式匹配*/
    val value3 = value1 match {
      case Some(s) => s
      case None => ""
    }
    println(value3)
  }
}
