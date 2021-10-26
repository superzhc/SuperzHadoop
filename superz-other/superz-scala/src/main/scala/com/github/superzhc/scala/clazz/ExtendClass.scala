package com.github.superzhc.scala.clazz

/**
 * Scala 也是使用 extends 关键字进行继承
 * 1. 子类中的方法要覆盖父类中的方法，必须使用 override
 * 2. 子类中的 val 属性要覆盖父类中的属性，必须写 override
 * 3. 父类中的变量（var）不可以覆盖
 * 4. fieldFunction 是抽象类的一个方法，这有点类似语法糖，一般属性都会有个同名的方法
 *
 * @author superz
 * @create 2021/9/9 15:34
 */
class ExtendClass(
                   /* 注意：对于父类的主构造参数不要用val或var修饰了 */
                   name: String, age: Int, val sex: String, override val fieldFunction: String) extends ParentClass(name, age) {
  var mark: String = _

  /* 在 Scala 中重写一个非抽象方法必须使用 override 修饰符 */
  override def overrideMethod = {
    // 调用父类的方法
    println(super.overrideMethod)
    "继承类重写了方法"
  }
}
