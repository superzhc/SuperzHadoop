package com.github.superzhc.scala.traits

/**
 * 1. Java 的所有接口都可以当作 Scala 特质来使用
 *
 * @author superz
 * @create 2021/9/9 16:02
 */
class CommonClass
/* 对于特质的继承使用 extends，而不是 implements*/
  extends SimpeTrait
    /* 如果需要的特质不止一个，可以用with 关键字来添加额外的特质 */
    with Serializable {

  /* 重写特质的抽象方法时 override 可写可不写 */
  override def log(msg: String): Unit = log2(msg)

  override var variable: String = _
}

object CommonClass {
  def main(args: Array[String]): Unit = {
    val commonClass=new CommonClass
    commonClass.variable="变量"
    println(commonClass.variable)
    println("-------------------------------华丽的分割线-----------------------------------")
    println(commonClass.constant)
  }
}
