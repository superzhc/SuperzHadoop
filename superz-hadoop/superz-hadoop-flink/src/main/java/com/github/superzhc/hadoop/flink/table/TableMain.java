package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
//import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * Apache Flink 有两种关系型 API 来做流批统一处理：Table API 和 SQL。
 * - Table API 是用于 Scala 和 Java 语言的查询API，它可以用一种非常直观的方式来组合使用选取、过滤、join 等关系型算子。
 * - Flink SQL 是基于 Apache Calcite 来实现的标准 SQL。
 *
 * @author superz
 * @create 2021/10/13 16:29
 */
public class TableMain {
    /**
     * TableEnvironment
     *
     * TableEnvironment 是 Table API 和 SQL 集成的核心概念，它主要负责:
     * 1. 在内部 catalog 中注册一个 Table;
     * 2. 注册外部的 catalog;
     * 3. 执行 SQL 查询
     * 4. 注册用户自定义函数(scalar、table 或 aggregation)
     * 5. 将 DataStream 或者 DataSet 转换成 Table
     * 6. 持有 ExecutionEnvironment 或者 StreamExecutionEnvironment 的引用
     *
     * 一个 Table 总是会绑定到一个指定的 TableEnvironment 中，相同的查询不同的 TableEnvironment 是无法通过 join、union 合并在一起。
     *
     * TableEnvironment 有一个在内部通过表名组织起来的表目录，Table API 或者 SQL 查询可以访问注册在目录中的表，并通过名称来引用它们。
     */

    /**
     * TableEnvironment 允许通过各种源来注册一个表:
     * 1、一个已存在的Table对象，通常是Table API或者SQL查询的结果 Table projTable = tableEnv.scan("X").select(...);
     * 2、TableSource，可以访问外部数据如文件、数据库或者消息系统 TableSource csvSource = new CsvTableSource("/path/to/file", ...);
     * 3、DataStream或者DataSet程序中的DataStream或者DataSet //将DataSet转换为Table Table table= tableEnv.fromDataSet(tableset);
     */

    /**
     * 注册TableSink可用于将 Table API或SQL查询的结果发送到外部存储系统，例如数据库，键值存储，消息队列或文件系统
     */

    public static void main(String[] args) {
        /**
         * Table API 和 SQL 程序的通用结构
         */
        /* 设置执行计划，如果 classpath 中包含两个 Table Planner，此处需要指定 */
        EnvironmentSettings envSetting = null;
        envSetting = EnvironmentSettings.newInstance()
                // Planner 和 Model
//                .useOldPlanner().inStreamingMode()//
//                .useOldPlanner().inBatchMode()//
//                .useBlinkPlanner().inStreamingMode()//
//                .useBlinkPlanner().inBatchMode()//
                /* 如果/lib目录中只有一种计划器的 jar 包，则可以使用useAnyPlanner */
//                .useAnyPlanner().inStreamingMode()
//                .useAnyPlanner().inBatchMode()
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         * 1. 创建 TableEnvironment
         * TableEnvironment 可以通过静态方法 BatchTableEnvironment.create() 或者 StreamTableEnvironment.create() 在 StreamExecutionEnvironment 或者 ExecutionEnvironment 中创建，TableConfig 是可选项。TableConfig 可用于配置 TableEnvironment 或定制的查询优化和转换过程。
         */
        TableEnvironment tableEnv = StreamTableEnvironment.create(env, envSetting);
        // 或
//        ExecutionEnvironment env2 = ExecutionEnvironment.getExecutionEnvironment();
//        TableEnvironment tableEnv2 = BatchTableEnvironment.create(env2);

        /**
         * 2. 创建表
         * 在 SQL 的术语中，Table API 的对象对应于视图（虚拟表）。它封装了一个逻辑查询计划。
         */
        /* 方式一 */
        // create an input Table
        tableEnv.executeSql("CREATE TEMPORARY TABLE table1 ... WITH ( 'connector' = ... )");
        // register an output Table
        tableEnv.executeSql("CREATE TEMPORARY TABLE outputTable ... WITH ( 'connector' = ... )");

        /* Connector Table：通过 connector 声明来创建 Table (方式二) */
//        tableEnv
//                /* 已被废弃，在 1.12 之后版本可能被重构，推荐使用 executeSql 方式来注册表 */
//                .connect(null)
//                .withFormat(null)
//                .withSchema(null)
//                .inAppendMode()
//                .createTemporaryTable("");

        /**
         * 3. 查询表
         */
        /* Table API */
        Table table2 = tableEnv.from("table1")
                .where($("id").isGreater("10"))//
                .groupBy($("age"), $("sex"))//
                .select($("age"), $("sex"), $("name").count().as("nums"));
        /* SQL
         * Flink SQL 是基于实现了SQL标准的 Apache Calcite 的。
         * SQL 查询由常规字符串指定。
         *  */
        // 上面的功能等同于如下
        Table table3 = tableEnv.sqlQuery("SELECT age,sex,count(name) FROM table1 where id > 10 group by age,sex ");

        /**
         * 输出表
         * Table 通过写入 TableSink 输出。
         * TableSink 是一个通用接口，用于支持多种文件格式（如 CSV、Apache Parquet、Apache Avro）、存储系统（如 JDBC、Apache HBase、Apache Cassandra、Elasticsearch）或消息队列系统（如 Apache Kafka、RabbitMQ）。
         * 批处理 Table 只能写入 BatchTableSink，而流处理 Table 需要指定写入 AppendStreamTableSink，RetractStreamTableSink 或者 UpsertStreamTableSink。
         */

        // 方法 Table.executeInsert(String tableName) 将 Table 发送至已注册的 TableSink。
        TableResult tableResult = table2.executeInsert("outputTable");
        //tableResult...
    }
}
