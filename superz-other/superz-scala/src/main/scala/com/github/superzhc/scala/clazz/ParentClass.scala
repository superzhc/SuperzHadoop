package com.github.superzhc.scala.clazz

/**
 * @author superz
 * @create 2021/9/9 15:34
 */
abstract class ParentClass(val name: String, val age: Int) {
  def fieldFunction: String

  def overrideMethod = "ParentClass 定义的待重写方法"
}
