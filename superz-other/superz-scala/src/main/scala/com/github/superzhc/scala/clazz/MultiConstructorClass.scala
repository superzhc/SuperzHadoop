package com.github.superzhc.scala.clazz

/**
 * 多构造器
 * Scala可以有任意多的构造器，但主构造器只有一个，可以有任意多的辅助构造器
 *
 * @author superz
 * @create 2021/9/9 13:49
 */
class MultiConstructorClass /*private*/
/*此处放置private关键字，这会让主构造器变成私有的*/ (
                                  /**
                                   * 主构造器
                                   * 1. 在 Scala 中，每个类都有主构造器
                                   * 2. 主构造器的参数直接放置在类名之后
                                   * 3. 如果一个类没有显式定义主构造器则自动拥有一个无参的主构造器
                                   */
                                  /* 主构造器的参数被编译成字段，其值被初始化成构造时传入的参数 */
                                  /* name、age 是私有字段，但提供公有的 getter/setter 方法*/
                                  val name: String
                                  , var age: Int
                                  /* 可以在主构造器中使用默认参数来避免过多地使用辅助构造器 */
                                  , var sex: String = "男"
                                  /* 该语句会声明一个 private var mark1:String 属性，因此不仅字段是私有的，getter/setter 也是私有的 */
                                  , private var mark1: String = "备注1"
                                  /* 如果不带val或var的参数至少被一个方法所使用，它将被升格为字段 */
                                  /* mark2 是不可变字段，且这个字段是对象私有的，等同于 `private[this] val mark2`*/
                                  , mark2: String = "备注2"
                                  /* mark3 未被任何方法调用，该参数将不被保存为字段，它仅仅是一个可以被主构造器中的代码访问的普通参数 */
                                  , mark3: String = "备注3"
                                ) {

  /* 主构造器会执行类定义中的所有语句 */
  println(this.getClass.toString + " instance.")
  println(mark3)

  /**
   * 辅助构造器
   * 1. 辅助构造器的名称为 this
   * 2. 每个辅助构造器都必须以一个对先前已定义的其他辅助构造器或主构造器的调用开始
   */
  def this(name: String) {
    this(name, 28) // 调用主构造器
  }

  def useMark2 = mark2

  /**
   * 编译后代码：【部分】
   * private final String name;
   *
   * private int age;
   *
   * private String sex;
   *
   * private String mark1;
   *
   * private final String mark2;
   *
   * public MultiConstructorClass(String name, int age, String sex, String mark1, String mark2, String mark3) {
   * Predef$.MODULE$.println((new StringBuilder()).append(getClass().toString()).append(" instance.").toString());
   * Predef$.MODULE$.println(mark3);
   * }
   *
   * public String name() {
   * return this.name;
   * }
   *
   * public int age() {
   * return this.age;
   * }
   *
   * public void age_$eq(int x$1) {
   * this.age = x$1;
   * }
   * ....
   * private String mark1() {
   * return this.mark1;
   * }
   *
   * private void mark1_$eq(String x$1) {
   * this.mark1 = x$1;
   * }
   * ...
   */
}
