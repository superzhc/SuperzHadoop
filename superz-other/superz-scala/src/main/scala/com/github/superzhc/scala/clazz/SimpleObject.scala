package com.github.superzhc.scala.clazz

/**
 * 对象
 * 在 Scala 中没有静态方法或静态字段，但可以通过 object 来实现相同的目的
 *
 * @author superz
 * @create 2021/9/9 14:57
 */
object SimpleObject {
  /**
   * 对象
   * 1. 作为存放工具函数或常量的地方
   * 2. 高效地共享单个不可变实例
   * 3. 需要用单个实例来协调某个服务时
   */
  private val field = "DEMO"

  val feild2 = "TEST"

  def test(str: String): Unit = {
    println(str)
  }
}
