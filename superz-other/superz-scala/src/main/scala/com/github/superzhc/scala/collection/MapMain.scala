package com.github.superzhc.scala.collection

/**
 * 映射
 *
 * @author superz
 * @create 2021/9/8 18:26
 */
object MapMain {
  /* 构造一个映射 */
  // 下面代码构造出一个不可变的 `Map[String,Int]`，其值不能被改变
  val people = Map("张三" -> 29, "李四" -> 28, "王五" -> 30)
  // 也可以使用元组的方式来定义映射
  val p2 = Map(("张三", 29), ("李四", 28), ("王五", 30))
  // 构造可变映射
  val p3 = scala.collection.mutable.Map("张三" -> 29, "李四" -> 28, "王五" -> 30)

  def main(args: Array[String]): Unit = {
    /* 获取映射中的值 */
    println(people("李四"))
    // 若不存在则使用默认值
    println(people.getOrElse("赵六", 27))

    /* 更新可变映射的值 */
    // people("张三") = 28 // × 编译器会提示报错
    p3("张三") = 28
    println(p3)
    // 可以使用+=操作添加多个映射
    p3 += ("赵六" -> 27, "钱七" -> 28)
    println(p3)
    // 使用-=来移除某个键和对应的值
    p3 -= "李四"
    println(p3)

    /* 迭代映射 */
    for ((k, v) <- p3) {
      printf("%s:%d\n", k, v)
    }
    // 获取所有的key
    println(people.keySet)
    // 获取所有的value
    println(people.values)
  }
}
