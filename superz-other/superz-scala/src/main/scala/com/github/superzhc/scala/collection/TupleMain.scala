package com.github.superzhc.scala.collection

/**
 * 元组
 * n个对象的集合，但并不一定要相同的类型
 *
 * @author superz
 * @create 2021/9/8 18:27
 */
object TupleMain {
  /* 元组的值是通过将单个值包含在圆括号中构成的 */
  val t3: Tuple3[Int, Double, String] = (1, 3.14, "Superz")

  def main(args: Array[String]): Unit = {
    /* 访问元组数据 */
    println(t3._1)
    println(t3._2)
    println(t3._3)
  }
}
