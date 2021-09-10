package com.github.superzhc.scala.implicits

/**
 * 隐式转换
 *
 * @author superz
 * @create 2021/9/9 19:54
 */
object ImplicitMain {
  /* 隐式转换函数：以 implicit 关键字声明的带有单个参数的函数，这种函数将被自动应用，将值从一种类型转换为另一种类型 */
  // 注意：推荐使用 source2Target 这种约定俗成的命名方式
  implicit def int2String(n: Int) = n.toString + "@Superz"

  /* 隐式参数：函数或方法可以带有一个标记为 implicit 的参数列表。这种情况下，编译器将会查找缺省值，提供给该函数或方法 */
  case class Parameter(p: String)

  /* 隐式常量 */
  implicit val parameter = Parameter("Superz")

  /* 隐式参数：函数或方法可以带有一个标记为 implicit 的参数列表。这种情况下，编译器将会查找缺省值，提供给该函数或方法 */
  def intParamter(n: Int)(implicit parameter: Parameter) = n.toString + "@" + parameter.p

  /**
   * 隐式转换规则
   * 1. 当表达式的类型与预期的类型不同时；
   * 2. 当对象访问一个不存在的成员时
   * 3. 当对象调用某个方法，而该方法的参数声明与传入参数不匹配时
   */

  /**
   * 不使用隐式转换规则
   * 1. 如果代码能够在不使用隐式转换的前提下通过编译，则不会使用隐式转换
   * 2. 编译器不会尝试同时执行多次转换，比如convert1(convert2(a))*b
   * 3. 存在二义性的转换是个错误，这种情况编译器就会报错
   */

  def main(args: Array[String]): Unit = {
    val i: Int = 1
    // 下面的代码就调用了上面的 int2String 的转换
    println(i.substring(0))
    println(intParamter(10))
  }
}
