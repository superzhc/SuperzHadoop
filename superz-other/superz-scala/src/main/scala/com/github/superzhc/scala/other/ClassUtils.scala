package com.github.superzhc.scala.other

/**
 * @author superz
 * @create 2021/9/9 15:42
 */
object ClassUtils {
  def main(args: Array[String]): Unit = {
    val p: String = ""
    /* 判断对象是否属于某个给定的类 */
    println(p.isInstanceOf[String])
    /* 进行类型转换 */
    val p2 = p.asInstanceOf[String]
    println(p2.isInstanceOf[String])
  }
}
