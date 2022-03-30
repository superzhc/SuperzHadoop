# 集合

Scala 提供了一套集合的实现，提供了一些集合类型的抽象

Scala 集合分为 **可变（mutable）** 和 **不可变（immutable）** 的集合

- 不可变集合：代表这个集合本身的内存空间大小不允许进行动态变化
- 可变集合：指这个集合所占的内存空间大小可以动态发生变化

可变集合可以在适当的地方被更新或扩展，这意味着可以修改，添加，移除一个集合的元素。

> 所有的集合类都位于包 `scala.collection`， `scala.collection.mutable`，`scala.collection.immutable`，`scala.collection.generic` 中。
> 
> 默认情况下，Scala 一直采用不可变集合类。

## 数组

### 定长数组

如果需要一个长度不变的数组，可以用 Scala 中的 Array。

**示例**

```scala
// 10个整数的数组，所有元素的初始化为0
val nums=new Array[Int](10)
// 10个元素的字符串数组，所有元素初始化为null
val s=new Array[String](10)
// 长度为2的Array[String]--类型是推断出来的；说明：已提供初始值就不需要new
val s=Array("Hello","World")
// 使用()而不是[]来访问元素
s(0)="Goodbye"
// 结果：Array("Goodbye","World")
```

在 JVM 中，Scala 的 Array 以 Java 数组的方式实现。

## 变长数组：数组缓冲

对于那种长度按需要变化的数组，Scala 中的等效数据结构为 ArrayBuffer

```scala
import scala.collection.mutable.ArrayBuffer

// 一个空的数组缓冲，准备存放整数
val b=ArrayBuffer[Int]() 

// 用+=在尾端添加元素
b += 1

// 在尾端添加多个元素，用括号括起来
b += (1,2,3,4,5)

// 用++=操作符追加任何集合
b ++= Array(8,13,21)

// 移处最后5个元素
b.trimEnd(5)
```

在数据缓冲的尾端添加或移处元素是一个高效的操作。

也可以在任意位置插入或移处元素，但这样的操作并不那么的高效--所有在那个位置之后的元素都必须被平移。

```scala
// 在下标2之前插入
b.insert(2,6)
//可以插入任意多的元素
b.insert(2,7,8,9)
b.remove(2)
//从下标2开始移处3个元素。第二个参数的含义是要移除多少个元素
b.remove(2,3)
```

**ArrayBuffer 的符号运算**

|  符号   | 作用                                               |
|:-----:|--------------------------------------------------|
| `++`  | 用于合并 ArrayBuffer 或者 Array                        |
| `+=`  | 将符合类型的元素添加到此 ArrayBuffer 中                       |
| `++=` | 将另一个 Array 或者 ArrayBuffer 内的元素添加到原 ArrayBuffer 内 |
| `-=`  | 删除一个指定的元素。如果该元素并不存在，则该操作不会报错，也不会引起变化             |
| `--=` | 删掉和另一个 Array 或者 ArrayBuffer 内重复的元素               |

> 有时需要构建一个Array，但不知道最终需要装多少元素的情况下，先构建一个缓冲数组，然后调用`b.toArray`即可获得一个数组。反之，调用`a.toBuffer`可以将一个数组a转换成一个数组缓冲

### 变长数组与定长数组之间的转换

定长数组 Array 提供 toBuffer 方法转换为变长数组 Buffer （它是特质）。

变长数组 ArrayBuffer 和 toArray 方法转换为定长数组 Array。

```scala
val array : Array[Int] = Array[Int](1,2,3,4)
val arrayBuffer : ArrayBuffer[Int] = ArrayBuffer[Int](5,6,7,8)

//toBuffer方法返回的是一个 scala.collection.mutable.Buffer 类型。
val toBuffer: Buffer[Int] = array.toBuffer[Int]

// 可以通过强制转换得到更具体的 ArrayBuffer，而不是 Buffer 。
// val toBuffer: ArrayBuffer[Int] = array.toBuffer[Int].asInstanceOf[ArrayBuffer[Int]]

//检查这个 toBuffer 也可以发现它实际是 ArrayBuffer 类型的上转型对象。
println(toBuffer.getClass)

//array 本身没有发生变化。输出会得到[I，因为它本质上是Java的Int[]数组。
println(array.getClass)

//ArrayBuffer[T] 可以通过 toArray 方法转换为 Array[T]。
val toArray: Array[Int] = arrayBuffer.toArray

//arrayBuffer 本身也没有变化。输出会得到 scala.collection.mutable.ArrayBuffer。
println(arrayBuffer.getClass)
```

## Seq

Seq 是一个特征, 代表可以保证不变的索引序列。你可以使用元素索引来访问元素。它保持元素的插入顺序。

Seq 可细分为索引序列和线性序列。一般来说索引序列的访问速度比线性序列要快一些，但是线性序列的应用场景比较多，比如队列和栈等都是很常用的数据结构。

### List

```scala
//简单创建一个集合
val list : List[Int] = List(1,2,3,4,5)
//打印list会显示List(1,2,3,4,5)
print(list)

//创建一个空集合,Nil是一个独立的类，它继承于List[Nothing].
val nil: Nil.type = Nil
//打印Nil会打印List()
println(nil)
```

List 内同样也可以放入不限于一种数据类型，但是这需要声明该 List 的泛型为 Any。访问 List 元素的方式也和 Array 相仿，使用圆括号 (index)。

```scala
//访问 list 的第 3 个元素，下标从 0 开始。
print(list(2))
```

**List 操作**

> 由于 List 本身是不可改变的，因此追加操作不会改变原来的 List ，而是返回一个新的 List。

```scala
//在列表后面追加元素，使用函数 :+ ，且 list 写在前面。
val addTohead: List[Int] = list :+ 0

//在列表前面追加元素，使用函数 +: ，且 list 写在后面。
val addToTail: List[Int] = 6 +: list

//除了头部插入，尾部插入之外，使用 :: 符号可以实现将多个零散的元素拼接到 List 当中。
val tail = List(3,4,5)
val ints: List[Int] = 1 :: 2 :: tail

//:: 符号的运算顺序是由右到左，因此使用该符号进行拼接时，一定要保证表达式最右边是 List 类型。如果没有可用做容器的 List ，则可以使用 Nil 代替，表示将拼接的元素装入到一个新的空 List 当中：
val ints2: List[Int] = 1 :: 2 :: 3 :: Nil

// ::: 表示装入的是某个List内的所有元素，而不是这个List本身
val multi_list: List[Any] = list_1 ::: list_2 ::: list_3
print(s"list_3's size = ${multi_list.size}")

// ::和:::是可以混用的，运算顺序总是从右到左。
val multi_list2: List[Any] = list_1 ::: list_2 :: list_3 :: Nil
print(s"list_3's size = ${multi_list2.size}")
for (index <- multi_list2.indices) {println(multi_list(index))}
```

### ListBuffer

ListBuffer 是可变长度的列表，属于 Seq 序列。它与不可变的 List 不同的是，所有的添加、删除操作会在原列表里进行，而不是返回一个新的列表。

```scala
//使用 new 调用构造器构建一个空对象。
val ints = new ListBuffer[Int]()
ints.append(1)

//使用静态的apply方法构建。
val ints1: ListBuffer[Int] = ListBuffer[Int](1, 2, 3, 4)
ints1.append(2)

// ListBuffer 提供了追加元素的方法：append，或者更精简的符号 +=。

//如果只添加一个元素，一般用+=。
ints1 += 2

//append 方法支持可变参数 Any*,因此用于一次性追加多个元素。
ints1.append(3,4,5,6)

// 使用 ++= 来将其它 **ListBuffer 内的所有元素 **全部追加进来

ints1 ++= ints
```

## Set

> 集（Set）是不重复元素的集合，它不保证元素的顺序。Scala 中的 Set 同样分为可变(mutable)的和不可变(immutable)的，默认情况下是不可变的。

```scala
//创建一个不可变的Set集合。
val immutableInts = Set(1,2,3,4)

//创建一个可变的Set集合。
val mutableInts = mutable.Set(1,2,3,4,5)

//向集合中添加一个元素5。
mutableInts += 5

//使用add方法也可以向集合中添加一个元素。
//也可以这样声明：mutableInts add 6 
mutableInts.add(6)

//删除集合中的元素,如果没有改元素，则什么都不会发生。
mutableInts -= 5

//使用remove方法也可以删除集合中的元素。
mutableInts remove 5

```

## Map

Scala 的 Map 分为 可变(mutable) 和 不可变(immutable) 的。其中可变的 Map 是无序的，而不可变的 Map 则是有序的。默认情况下使用的是不可变的 Map ，如果要使用可变的 Map 则需要加上前缀加以区分。

```scala
//由于本质上 k-v 对是一个元组，因此也可以写成括号的形式。
//注意箭头的形式为 "->", 而非 "=>"。
val mapSample = mutable.Map("Kotlin" -> "2020", ("Go", "2019"), "Python" -> "2018", "Java" -> "2017")

//Map 的取值直接使用小括号即可。括号内的参数是所查找的 value 所对应的 key 值。
val map = mutable.Map("scala"->"2019","go"->"2018")

//不需要像java一样调用get方法，也可以直接取出value值。
//当然Scala也提供了传统的get方法，且和java有一些区别，我们稍后再探讨它。
print(map("scala"))

//通过+=符号向Map当中追加键值对。
map += "Kotlin" -> "2017"
//或者使用元组的方式追加键值对。注意，要嵌套两层括号，表示放入的是一个包含二元组的集合。
// map += (("Kotlin"->"2017"))

println(map.getOrElse("Kotlin", "()"))

//如果添加了一个重复的key,程序会报错嘛？
map += "scala" -> "2020"
println(map.getOrElse("scala", "()"))

//通过-= "key" 删掉map中存储的key-value对。
map -= "go"
println(map.getOrElse("go", "deleted"))

//如果正企图删除一个不存在的key，程序会报错嘛？
map -= "Java"

//将原先key = scala 的value从2019 修改为2020.
map("scala") = "2020"

//尝试打印出scala对应的value值。
println(map.getOrElse("scala", "()"))

//如果为一个不存在的key赋值呢？
map("Java") = "2014"

//会提示错误嘛？
println(map.getOrElse("Java", "()"))

//如果要在遍历过程中同时使用key和value:
for ((k, v) <- map) {
  println(s"key:$k,value=$v")
}

println("---------------------------")
//如果要在遍历过程中只使用key:
for (k <- map.keys) {
  println(s"key:$k")
}

println("---------------------------")
//如果要在遍历过程中只使用value:
var count = 1
for (v <- map.values) {
  println(s"$count element value=$v")
  count += 1
}

println("---------------------------")
//如果要在遍历过程中将这个键值对视作是Tuple2:
for ( tuple <- map) {
  println(s"key:${tuple._1},value=${tuple._2}")
}
```

## Scala 和 Java 之间的集合转换

**ArrayBuffer (Scala) -> List (Java)**

Scala 提供了一个可以将 ArrayBuffer（该类属于 Scala）转换为 List (该类属于 Java) 的隐式转换函数。

```scala
import scala.collection.JavaConversions.bufferAsJavaList
```

**List (Java) -> Scala (ArrayBuffer)**

```scala
import scala.collection.JavaConversions.asScalaBuffer
```