package com.github.superzhc.scala.functions

/**
 * @author superz
 * @create 2021/9/8 17:19
 */
object FunctionMain {
  def main(args: Array[String]): Unit = {
    // fun1()
    println(decorate("superz", right = ">"))
    println(sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    // 如果传入一个整数区间，Scala 编译器无法识别这整数区间是一个参数还是可变参数的数组，因此会报错，可使用如下的方式将一个整数区间作为参数传入可变参数中
    println(sum(1 to 10: _*))
    box("Hello World")
  }

  /**
   * 函数定义
   * def functionName(parameter:parameterType[,...]):returnType={
   * // 函数体
   * }
   *
   * 参数的类型是必须添加的，但是返回值类型可以不用指定，Scala 编译器可以通过表达式推导出返回值类型
   */
  def fun1(): Unit = {
    println("这是一个函数")
  }

  /* 带有默认值的参数，调用的时候可以使用参数的名称 */
  def decorate(str: String, left: String = "[", right: String = "]"): String = left + str + right

  /* 可变参数 */
  def sum(args: Int*) = {
    var result = 0
    for (arg <- args) result += arg
    result
  }

  /**
   * 过程
   * Scala 对于不返回值的函数有特殊的表示法。如果函数体包含在花括号中但不使用 = 将函数定义和花括号连接的，那么返回类型就是 Unit。这样的函数被称之为 过程（procedure）。
   */
  def box(s: String) {
    var border = "-" * s.length + "--\n"
    println(border + "|" + s + "|\n" + border)
  }
}
