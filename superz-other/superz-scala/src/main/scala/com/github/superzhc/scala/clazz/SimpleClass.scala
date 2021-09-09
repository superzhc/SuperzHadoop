package com.github.superzhc.scala.clazz

import java.util.Date
import scala.beans.BeanProperty

/**
 * 简单类
 *
 * @author superz
 * @create 2021/9/9 10:18
 */
class SimpleClass {
  /**
   * 在 Scala 中，类并不声明为 public。Scala 源文件可以包含多个类，所有这些类都具有公有可见性
   */

  private var value = 0 //必须初始化属性

  /**
   * 方法默认是公有的
   */
  def increment() {
    value += 1
  }

  /**
   * 对于这种无参的方法，调用的时候可以写上括号，也可以不用写上
   * 示例：instance.current 或 instance.current()
   *
   * @return
   */
  def current() = value

  /**
   * 对于定义无参的方法不带括号的，调用的时候也不可以带括号（强制性）
   */
  def current2 = value

  /**
   * 对于无参调用的方式，推荐对于改值器（改变对象的状态）的方法带入括号；对于取值器（不会改变对象的状态）的方法去掉括号
   */

  // region getter/setter属性
  /**
   * Scala对每个字段都提供getter和setter方法
   */
  // Scala 生成 Class 文件时，会发现其中有个私有的 age 字段以及相应的 getter和setter方法，这两个方法是公有的
  /**
   * 编译后代码：
   * private int age = 0;
   *
   * public int age() {
   * return this.age;
   * }
   *
   * public void age_$eq(int x$1) {
   * this.age = x$1;
   * }
   */
  var age = 0

  // 对于私有字段而言，getter和setter方法也是私有的
  /**
   * 编译后代码：
   * private int age2 = 0;
   *
   * private int age2() {
   * return this.age2;
   * }
   *
   * private void age2_$eq(int x$1) {
   * this.age2 = x$1;
   * }
   */
  private var age2 = 0

  /* 重新定义age2的getter和setter */

  /**
   * 编译后代码：
   * public int ageX() {
   * return age2();
   * }
   *
   * public void ageX_$eq(int newValue) {
   * age2_$eq(newValue);
   * }
   * 注意JVM不允许方法中出现=号，所以用$eq来进行了替代
   */
  /*
  使用：
  赋值 simpleClass.ageX = 28
  取值simpleClass.ageX
  */
  def ageX = age2

  def ageX_=(newValue: Int): Unit = {
    age2 = newValue
  }

  /**
   * 只带getter的属性:
   * 使用val定义变量，Scala会生成一个私有的final字段和一个getter方法，但没有setter
   * */
  /**
   * 编译后代码：
   * private final Date timestamp = new Date();
   *
   * public Date timestamp() {
   * return this.timestamp;
   * }
   */
  val timestamp = new Date()

  // endregion

  // region Bean 属性
  /**
   * 编译后代码：
   * private String name;
   *
   * public String name() {
   * return this.name;
   * }
   *
   * public void name_$eq(String x$1) {
   * this.name = x$1;
   * }
   *
   * public void setName(String x$1) {
   * this.name = x$1;
   * }
   *
   * public String getName() {
   * return name();
   * }
   */
  @BeanProperty var name: String = _
  //endregion
}
