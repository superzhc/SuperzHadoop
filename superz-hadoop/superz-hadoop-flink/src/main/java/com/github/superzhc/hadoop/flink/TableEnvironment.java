package com.github.superzhc.hadoop.flink;

/**
 * TableEnvironment
 * TableEnvironment 是用来创建 Table & SQL 程序的上下文执行环境 ，也是 Table & SQL 程序的入口，Table & SQL 程序的所有功能都是围绕 TableEnvironment 这个核心类展开的。
 * TableEnvironment 的主要职能包括：对接外部系统，表及元数据的注册和检索，执行SQL语句，提供更详细的配置选项。
 * <p>
 * Flink 1.9 中保留了 5 个 TableEnvironment，在实现上是 5 个面向用户的接口，在接口底层进行了不同的实现。5 个接口包括一个 TableEnvironment 接口，两个 BatchTableEnvironment 接口，两个 StreamTableEnvironment 接口，5 个接口文件完整路径如下：
 * 1. org/apache/flink/table/api/TableEnvironment.java
 * 2. org/apache/flink/table/api/java/BatchTableEnvironment.java
 * 3. org/apache/flink/table/api/scala/BatchTableEnvironment.scala
 * 4. org/apache/flink/table/api/java/StreamTableEnvironment.java
 * 5. org/apache/flink/table/api/scala/StreamTableEnvironment.scala
 * 结合文件的路径，梳理这 5 个接口，会发现 TableEnvironment 是顶级接口，是所有 TableEnvironment 的基类 ，BatchTableEnvironment 和 StreamTableEnvironment 都提供了 Java 实现和 Scala 实现 ，分别有两个接口。
 * <p>
 * TableEnvironment 作为统一的接口，其统一性体现在两个方面
 * 一是对于所有基于JVM的语言(即 Scala API 和 Java API 之间没有区别)是统一的；
 * 二是对于 unbounded data （无界数据，即流数据） 和 bounded data （有界数据，即批数据）的处理是统一的。
 * <p>
 * TableEnvironment 提供的是一个纯 Table 生态的上下文环境，适用于整个作业都使用 Table API & SQL 编写程序的场景。
 * TableEnvironment 目前还不支持注册 UDTF 和 UDAF，用户有注册 UDTF 和 UDAF 的需求时，可以选择使用其他 TableEnvironment。
 * <p>
 * 两个 StreamTableEnvironment 分别用于 Java 的流计算和 Scala 的流计算场景，流计算的对象分别是 Java 的 DataStream  和 Scala 的 DataStream。相比 TableEnvironment，StreamTableEnvironment 提供了 DataStream 和 Table 之间相互转换的接口，如果用户的程序除了使用 Table API & SQL 编写外，还需要使用到 DataStream API，则需要使用 StreamTableEnvironment。
 * 两个 BatchTableEnvironment 分别用于 Java 的批处理场景和 Scala 的批处理场景，批处理的对象分别是 Java 的 DataSet 和 Scala 的 DataSet。相比 TableEnvironment，BatchTableEnvironment 提供了 DataSet 和 Table 之间相互转换的接口，如果用户的程序除了使用 Table API & SQL 编写外，还需要使用到 DataSet API，则需要使用 BatchTableEnvironment。
 *
 * @author superz
 * @create 2021/10/18 14:35
 */
public class TableEnvironment {
}
