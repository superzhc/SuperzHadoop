package com.github.superzhc.scala

/**
  * 匹配模式示例
  */
object MatchDemo {
  def main(args: Array[String]): Unit = {
    println(matchTest("two"))
    println(matchTest("test"))
    println(matchTest(1))
    println(matchTest(6))

    val alice = Person("Alice", 25)
    val bob = Person("Bob", 32)
    val charlie = Person("Charlie", 32)
    val superz = Person("Superz", 28)

    for (person <- List(alice, bob, charlie, superz)) {
      person match {
        case Person("Alice", 25) => println("Hi Alice!")
        // case Person("Bob", 32) => println("Hi Bob!")
        case Person(name, 32) => println(s"Hi $name!")
        case Person(name, age) =>
          println("Age: " + age + " year, name: " + name + "?")
      }
    }
  }

  def matchTest(x: Any) = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int" // 类型判断
    case _ => "many"
  }

  case class Person(name: String, age: Int)

}
