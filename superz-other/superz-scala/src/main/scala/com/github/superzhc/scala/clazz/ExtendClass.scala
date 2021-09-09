package com.github.superzhc.scala.clazz

/**
 * Scala 也是使用 extends 关键字进行继承
 *
 * @author superz
 * @create 2021/9/9 15:34
 */
class ExtendClass(
                   /* 注意：对于父类的主构造参数不要用val或var修饰了 */
                   name: String, age: Int, val sex: String) extends ParentClass(name, age) {
  var mark: String = _

  /* 在 Scala 中重写一个非抽象方法必须使用 override 修饰符 */
  override def overrideMethod = {
    // 调用父类的方法
    println(super.overrideMethod)
    "继承类重写了方法"
  }
}
