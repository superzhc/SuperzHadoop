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
  /**
   * public class CaseClass implements Product, Serializable {
   * private final String name;
   *
   * private final int age;
   *
   * public static Option<Tuple2<String, Object>> unapply(CaseClass paramCaseClass) {
   * return CaseClass$.MODULE$.unapply(paramCaseClass);
   * }
   *
   * public static CaseClass apply(String paramString, int paramInt) {
   * return CaseClass$.MODULE$.apply(paramString, paramInt);
   * }
   *
   * public static Function1<Tuple2<String, Object>, CaseClass> tupled() {
   * return CaseClass$.MODULE$.tupled();
   * }
   *
   * public static Function1<String, Function1<Object, CaseClass>> curried() {
   * return CaseClass$.MODULE$.curried();
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
   * public CaseClass copy(String name, int age) {
   * return new CaseClass(name, age);
   * }
   *
   * public String copy$default$1() {
   * return name();
   * }
   *
   * public int copy$default$2() {
   * return age();
   * }
   *
   * public String productPrefix() {
   * return "CaseClass";
   * }
   *
   * public int productArity() {
   * return 2;
   * }
   *
   * public Object productElement(int x$1) {
   * int i = x$1;
   * switch (i) {
   * default:
   * throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
   * case 1:
   *
   * case 0:
   * break;
   * }
   * return name();
   * }
   *
   * public Iterator<Object> productIterator() {
   * return ScalaRunTime$.MODULE$.typedProductIterator(this);
   * }
   *
   * public boolean canEqual(Object x$1) {
   * return x$1 instanceof CaseClass;
   * }
   *
   * public String toString() {
   * return ScalaRunTime$.MODULE$._toString(this);
   * }
   *
   * public CaseClass(String name, int age) {
   * Product.class.$init$(this);
   * }
   * }
   */

  /**
   * public final class CaseClass$ extends AbstractFunction2<String, Object, CaseClass> implements Serializable {
   * public static final CaseClass$ MODULE$;
   *
   * public final String toString() {
   * return "CaseClass";
   * }
   *
   * public CaseClass apply(String name, int age) {
   * return new CaseClass(name, age);
   * }
   *
   * public Option<Tuple2<String, Object>> unapply(CaseClass x$0) {
   * return (x$0 == null) ? (Option<Tuple2<String, Object>>)scala.None$.MODULE$ : (Option<Tuple2<String, Object>>)new Some(new Tuple2(x$0.name(), BoxesRunTime.boxToInteger(x$0.age())));
   * }
   *
   * private Object readResolve() {
   * return MODULE$;
   * }
   *
   * private CaseClass$() {
   * MODULE$ = this;
   * }
   * }
   */
}
