package com.github.superzhc.scala.clazz

/**
 * @author superz
 * @create 2021/9/9 10:23
 */
object ClassUse {
  def main(args: Array[String]): Unit = {
    val simpleClass: SimpleClass = new SimpleClass // 或 new SimpleClass()
    simpleClass.increment()
    // 调用无参方法时，既可以写上括号，也可以不写
    println(simpleClass.current)
    println(simpleClass.current())

    simpleClass.age = 20 // 编译后：simpleClass.age_$eq(20)
    println(simpleClass.age) //编译后：simpleClass.age()

    simpleClass.ageX = 28
    println(simpleClass.ageX)

    println(simpleClass.timestamp)

    simpleClass.setName("张三")
    println(simpleClass.getName)

    val multiConstructorClass = new MultiConstructorClass("李四")
    println(multiConstructorClass.useMark2)

    val caseClass = CaseClass("王五", 28)
    println(caseClass)
  }
}
