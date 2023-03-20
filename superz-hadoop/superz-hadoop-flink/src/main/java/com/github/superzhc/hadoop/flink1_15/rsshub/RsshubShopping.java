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
public class RsshubShopping {
    public static void main(String[] args) {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");

        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoint");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoint");
        // conf.setString("table.exec.sink.not-null-enforcer","DROP");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.enableCheckpointing(1000);

        TableEnvironment tEnv = StreamTableEnvironment.create(env);

        String kafkaDDLSql = "CREATE TABLE rsshub_shopping(" +
                // "   `partition` BIGINT METADATA VIRTUAL," +
                "   title string," +
                "   description string," +
                "   guid string," +
                "   link string," +
                "   sourceType string," +
                "   syncDate date," +
                "   rsshubKey string," +
                "   pubDate date" +
                ") WITH (" +
                "   'connector' = 'kafka'," +
                "   'topic' = 'rsshub_shopping'," +
                "   'properties.bootstrap.servers' = 'localhost:19092'," +
                "   'properties.group.id' = 'flink_rsshub'," +
                "   'scan.startup.mode' = 'earliest-offset'," +
                "   'format' = 'json'," +
                "   'json.ignore-parse-errors' = 'true'" +
                ")";
        tEnv.executeSql(kafkaDDLSql);


        String icebergCatalogSql = "CREATE CATALOG hadoop_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hadoop'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='2'" +
                ")";
        tEnv.executeSql(icebergCatalogSql);

        tEnv.executeSql("INSERT INTO hadoop_catalog.rsshub.shopping /*+ OPTIONS('upsert-enabled'='true') */ (title,description,guid,link,sourceType,syncDate,rsshubKey,pubDate)" +
                " SELECT title,description,guid,link,sourceType,CAST(syncDate AS TIMESTAMP(6)),rsshubKey,CAST(pubDate AS TIMESTAMP(6)) FROM rsshub_shopping" +
                // " where guid is not null" +
                " ");
    }
}
