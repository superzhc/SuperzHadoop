# Scala 中下划线(`_`) 用法

## 匿名函数参数占位符

当匿名函数传递给方法或其他函数时，如果该匿名函数的参数在 `=>` 的右侧只出现一次，那么就可以省略 `=>`，并将参数用下划线代替。这对一元函数和二元函数都适用。例：

```scala worksheet
scala> val list = List(5,3,7,9,1)
list: List[Int] = List(5, 3, 7, 9, 1)

// list.map(x => x * 10)
scala> list.map(_ * 10)
res0: List[Int] = List(50, 30, 70, 90, 10)

// list.sortWith((x, y) => x < y)
scala> list.sortWith(_ < _)
res1: List[Int] = List(1, 3, 5, 7, 9)

// list.reduceLeft((x, y) => x + y)
scala> list.reduceLeft(_ + _)
res2: Int = 25
```

## 无用匿名函数参数

当匿名函数的参数未被实际使用到时，可以不给它一个命名，而直接用下划线代替。例：

```scala worksheet
scala> list.foreach(_ => println("Hello Scala"))
Hello Scala
Hello Scala
Hello Scala
Hello Scala
Hello Scala
```

## 泛型定义中的通配符

在 Java 中用问号来指代泛型中不确定类型的定义（如 `List<?>`）。Scala 用下划线来代替它，例：

```scala worksheet
scala> def testPrint(l: List[_]) = {
     |   list.foreach(x => println(x))
     | }
testPrint: (l: List[_])Unit
```

## 模式匹配中的通配符/占位符

得益于Scala模式匹配（pattern matching）的强大，下划线在模式匹配中的用途非常灵活。它除了可以用来代替Java switch-case表达方式中的default之外，还可以占位表示其他元素甚至类型。例：

```scala worksheet
str match {
  case 1 => "one"
  case 2 => "two"
  // 如同Java中的default
  case _ => "anything other than one and two"
}

expr match {
  // 以1开头，且长度为3的List
  case List(1,_,_) => "a list with three element and the first element is 1"
  // 长度大于等于0的List
  case List(_*)  => "a list with zero or more elements"
  // 键和值类型都为任意类型的Map
  case Map[_,_] => "matches a map with any key type and any value type"
  case _ =>
}
```

## 导入语句中的通配符

下划线可以实现Java import语句中星号的作用，但功能更强大一些。利用它还能导入时做重命名，以及忽略某些类。例：

```scala worksheet
// import java.util.concurrent.*
scala> import java.util.concurrent._
import java.util.concurrent._

// import java.util.*，并将ArrayList重命名为al
scala> import java.util.{ArrayList => al, _}
import java.util.{ArrayList=>al, _}

// import java.util.*，但不导入Timer类
scala> import java.util.{Timer => _, _}
import java.util.{Timer=>_, _}

// import static java.lang.Math.*
scala> import java.lang.Math._
import java.lang.Math._
```

## 变量默认值初始化

用下划线可以自动在变量声明时，将其赋予默认的初始值。例：

```scala worksheet
scala> var name : String = _
name: String = null

scala> var count : Int = _
count: Int = 0

scala> var avg : Double = _
avg: Double = 0.0
```

## 访问元组（tuple）

下划线后面跟上数字k，可以当作索引表示元组中的第k个元素。当要忽略元组中的某个值时，也可以用下划线代替它。例：

```scala worksheet
scala> val tuple = ("LMagics", 173.5, Seq(22,66,88))
tuple: (String, Double, Seq[Int]) = (LMagics,173.5,List(22, 66, 88))

scala> tuple._1
res1: String = LMagics

scala> tuple._2
res2: Double = 173.5

scala> tuple._3
res3: Seq[Int] = List(22, 66, 88)

scala> val (first, _, third) = tuple
first: String = LMagics
third: Seq[Int] = List(22, 66, 88)
```

## 声明setter方法

Scala中没有显式的setter方法，将getter方法的命名后加上一个下划线，就可以当作setter方法使用。例：

```scala worksheet
scala> class Test {
     |   private var pCount = 0
     |   def count = pCount  // getter
     |   def count_= (c : Int) = {  // setter
     |     require(c > 0)
     |     pCount = c
     |   }
     | }
defined class Test

scala> val test = new Test()
test: Test = Test@27d5a580

scala> test.count = 7
test.count: Int = 7
```

## 变长参数的转化

下划线与星号连用，可以将序列转化为变长参数的序列，方便调用变长参数的方法。例：

```scala worksheet
scala> def sum(args: Int*) = {
     |   var result = 0
     |   for (arg <- args) result += arg
     |   result
     | }
sum: (args: Int*)Int

scala> val sum1 = sum(7,8,9,10,11,12,13,14)
sum1: Int = 84

// 如果只写7 to 14，会报错
scala> val sum2 = sum(7 to 14: _*)
sum2: Int = 84
```