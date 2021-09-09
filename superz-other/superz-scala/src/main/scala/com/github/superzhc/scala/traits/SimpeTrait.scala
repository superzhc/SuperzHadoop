package com.github.superzhc.scala.traits

/**
 * 特质
 * 一个类扩展自一个或多个特质，以便使用这些特质提供的服务
 * Scala 提供的特质可以定义抽象方法，也可以定义具体方法
 *
 * @author superz
 * @create 2021/9/9 15:56
 */
trait SimpeTrait {
  /* 特质完全可以当作Java 接口来使用 */
  //  特质中未实现的方法默认就是抽象方法
  def log(msg: String) // 这是个抽象方法

  /* 带有具体实现的特质 */
  def log2(msg: String) {
    println(msg)
  }
}
