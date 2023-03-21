package com.github.superzhc.hadoop.flink1_15.rsshub;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2023/3/21 0:24
 */
public class FlinkRsshubShopping {
    public static void main(String[] args) {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");

        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoint");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoint");
        conf.setBoolean("table.dynamic-table-options.enabled", true);
        conf.setBoolean("table.exec.iceberg.use-flip27-source", true);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.enableCheckpointing(1000);

        TableEnvironment tEnv = StreamTableEnvironment.create(env);

        String kafkaDDLSql = "CREATE TABLE rsshub_shopping(" +
                "   title string," +
                "   description string," +
                "   guid string," +
                "   link string," +
                "   sourceType string," +
                "   syncDate TIMESTAMP(3)," +
                "   rsshubKey string," +
                "   pubDate TIMESTAMP(3)" +
//                // 设置主键
//                "   PRIMARY KEY(guid) NOT ENFORCED," +
//                // 设置WaterMark
//                "   WATERMARK FOR `pubDate` AS `pubDate` - INTERVAL '5' SECOND" +
                ") WITH (" +
                "   'connector' = 'kafka'," +
                "   'topic' = 'rsshub_shopping'," +
                "   'properties.bootstrap.servers' = 'localhost:19092'," +
                "   'properties.group.id' = 'flink_rsshub_new'," +
                "   'scan.startup.mode' = 'earliest-offset'," +
                "   'format' = 'json'," +
                "   'json.ignore-parse-errors' = 'true'" +
                ")";
        tEnv.executeSql(kafkaDDLSql);


//        String icebergCatalogSql = "CREATE CATALOG hadoop_catalog WITH (" +
//                "  'type'='iceberg'," +
//                "  'catalog-type'='hadoop'," +
//                "  'warehouse'='s3a://superz/flink/iceberg'," +
//                "  'property-version'='1'" +
//                ")";
//        tEnv.executeSql(icebergCatalogSql);

//        String icebergAlterSql = "ALTER TABLE hadoop_catalog.rsshub.shopping SET(" +
//                "   'format-version'='2'," +
//                "   'write.upsert.enabled'='true'" +
//                ")";
//        tEnv.executeSql(icebergAlterSql);

        // 注意：字段的顺序需要和创建表的schema一致，不然报错
        String icebergDDLSql = "CREATE TABLE IF NOT EXISTS shopping_connector(" +
                "   title string," +
                "   description string," +
                "   guid string," +
                "   link string," +
                "   sourceType string," +
                "   syncDate TIMESTAMP(3)," +
                "   rsshubKey string," +
                "   pubDate TIMESTAMP(3)," +
                "   PRIMARY KEY(guid,rsshubKey) NOT ENFORCED" +
                ") WITH (" +
                "    'connector'='iceberg'," +
                "    'catalog-type'='hadoop'," +
                "    'catalog-name'='hadoop'," +
                "    'catalog-database'='rsshub'," +
                "    'catalog-table'='shopping'," +
                "    'warehouse'='s3a://superz/flink/iceberg'" +
//                "   'format-version'='2'," +
//                "   'write.upsert.enabled'='true'" +
                ")";
        tEnv.executeSql(icebergDDLSql);


        tEnv.executeSql("INSERT INTO shopping_connector" +
                // 已设置表参数：'write.upsert.enabled'='true'，无需使用一下选项
//                " /*+ OPTIONS('upsert-enabled'='true') */" +
//                " (title,description,guid,link,sourceType,syncDate,rsshubKey,pubDate)" +
                " SELECT " +
//                " title,description,guid,link,sourceType,syncDate,rsshubKey,pubDate" +
                " * " +
                " FROM rsshub_shopping" +
                // " where guid is not null" +
                " ");
    }
}
