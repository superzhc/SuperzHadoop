package com.github.superzhc.hadoop.flink.iceberg.catalog;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/30 23:24
 */
public class FlinkIcebergHiveCatalogInS3Test {
    StreamExecutionEnvironment env;

    TableEnvironment tEnv;

    @Before
    public void setUp() {
        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoint");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoint");

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.enableCheckpointing(1000);

        tEnv = StreamTableEnvironment.create(env);
    }

    @Test
    public void catalog() {
        String sql = "CREATE CATALOG iceberg_hive WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hive'," +
                "  'uri'='thrift://127.0.0.1:9083'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'hadoop-conf-dir'='./target/classes'," +
                "  'clients'='5'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG iceberg_hive");
        tEnv.executeSql("SHOW DATABASES").print();
    }

    @Test
    public void createDatabase() {
        catalog();

        tEnv.executeSql("CREATE DATABASE flink");
    }

    @Test
    public void createTable() {
        catalog();

        String sql = "CREATE TABLE iceberg_hive.`default`.t_20230330 (" +
                "    id BIGINT COMMENT 'unique id'," +
                "    data STRING" +
                ")" +
                // 设置注释
                "   COMMENT '测试表'" +
                // 设置分区
                "   PARTITIONED BY (data)" +
                // table configuration
                "   WITH ('format-version'='2', 'write.upsert.enabled'='true')";
        tEnv.executeSql(sql);
    }

    public void createSupportUpsertTable(){
        catalog();

        String sql = "CREATE TABLE iceberg_hive.flink.t_20230330_001 (" +
                "    id BIGINT COMMENT 'unique id'," +
                "    data STRING" +
                // upsert表需要设置iceberg主键
                "    ,PRIMARY KEY(`id`) NOT ENFORCED" +
                ")" +
                // 设置注释
                "   COMMENT '测试表'" +
                // 设置分区
                "   PARTITIONED BY (data)" +
                // table configuration，对于upsert需要设置如下的参数
                "   WITH ('format-version'='2', 'write.upsert.enabled'='true')";
        tEnv.executeSql(sql);
    }

    // 读取已存在的表的效果会比较好
    @Test
    public void tableByConnector(){
        String sql="CREATE TABLE device_basic (" +
                "    code STRING," +
                "    name STRING," +
                "    full_name STRING," +
                "    sample_number INT," +
                "    publish_date DATE," +
                "    description STRING" +
                "   ,PRIMARY KEY(code) NOT ENFORCED" +
                ") WITH (" +
                "    'connector'='iceberg'," +
                "    'catalog-name'='iceberg_hive'," +
                "    'catalog-type'='hive'," +
                "    'catalog-database'='finance'," +
                "    'catalog-table'='index_basic'," +
                "    'uri'='thrift://127.0.0.1:9083'," +
                "    'hadoop-conf-dir'='./target/classes'," +
                "    'warehouse'='s3a://superz/finance'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("SELECT * FROM device_basic").print();
    }

}
