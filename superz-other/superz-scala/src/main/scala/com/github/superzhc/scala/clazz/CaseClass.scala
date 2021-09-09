package com.github.superzhc.scala.clazz

/**
 * 样例类
 * 1. 构造器中的每一个参数都变成为 val，除非它被显式地声明为 var（不推荐）
 * 2. 在伴生对象中提供 apply 方法，让开发不用 new 关键字就能构造出相应地对象
 * 3. 提供 unapply 方法让匹配模式可以工作
 *
 * @author superz
 * @create 2021/9/9 19:14
 */
case class CaseClass(name: String, age: Int) {

}
