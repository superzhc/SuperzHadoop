package com.github.superzhc.scala.annotation

import scala.beans.BeanProperty

/**
 * 注解将元信息与定义相关联
 *
 * @author superz
 * @create 2021/9/23 10:06
 */
object AnnotationMain {
  @deprecated("这是一个废除的方法", "0.2.0")
  def deprecated_method = "hello deprecated annotation"

  def main(args: Array[String]): Unit = {
    val obj = new DeprecatedClazz("张三三", 28)
    obj.hi()
    val obj2 = new DeprecatedClazz("李莉莉", 28, "女")
    obj2.hi()
  }
}

/**
 * 给类的主构造函数添加注解，需要在注解后面添加括号
 */
@MyAnnotation("test")
class DeprecatedClazz @deprecated()(
                                     @BeanProperty /* 标记生成 java 风格的 getter/setter 方法 */ var name: String
                                     , val age: Int, val sex: String) {
  def this(name: String, age: Int) {
    this(name, age, "男")
  }

  def hi() = {
    println(s"My name is $name , my age is $age , i am $sex")
  }
}

/**
 * Scala 要实现自己的注解，不要扩展 Annotation trait
 */
class MyAnnotation(
                    //注解参数
                    var value: String // 注解的参数是value的话，可以不用指定注解的参数名
                    , var param: String = ""
                  ) extends annotation.Annotation