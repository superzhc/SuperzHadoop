package com.github.superzhc.scala.clazz

/**
 * 伴生对象
 * 类和它的伴生对象可以相互访问私有特性，但它们必须存在于同一个源文件
 *
 * @author superz
 * @create 2021/9/9 15:08
 */
class CompanionObject(val name: String, val age: Int) {

}

/* 伴生对象 */
object CompanionObject {
  /**
   * apply 方法
   * 当遇到 `Object(参数1,...,参数N)` 这样的表达式时，apply 方法将会被调用
   */
  /*通常情况下，apply 方法返回的是伴生类的对象*/
  def apply(name: String, age: Int) = {
    new CompanionObject(name, age)
  }
}
