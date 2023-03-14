package com.github.superzhc.hadoop.flink.iceberg;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/14 15:21
 **/
public class FlinkIcebergDDLTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() throws Exception {
        // System.setProperty("aws.region", "us-east-1");
        System.setProperty("HADOOP_CONF_DIR","./hdfs-site.xml");

        Configuration conf = new Configuration();
//        conf.setString("fs.allowed-fallback-filesystems", "s3");
//        conf.setString("flink.hadoop.fs.s3a.endpoint", "http://127.0.0.1:9000");
//        conf.setBoolean("flink.hadoop.fs.s3a.path.style.access", true);
//        conf.setString("flink.hadoop.fs.s3a.access.key", "admin");
//        conf.setString("flink.hadoop.fs.s3a.secret.key", "admin123456");
//        conf.setBoolean("flink.hadoop.fs.s3a.ssl.enabled", false);
//        conf.setString("flink.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
//        FileSystem.initialize(conf, null);

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        tEnv = StreamTableEnvironment.create(env);
    }

    public void createHiveCatalog() {
        String sql = "CREATE CATALOG hive_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hive'," +
                "  'uri'='thrift://localhost:9083'," +
                "  'clients'='5'," +
                "  'property-version'='1'," +
                "  'warehouse'='hdfs://nn:8020/warehouse/path'" +
                ")";
    }

    @Test
    public void createHadoopCatalog() {
        String sql = "CREATE CATALOG hadoop_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hadoop'," +
//                "  'fs.s3a.endpoint'='http://127.0.0.1:9000'," +
//                "  'fs.s3a.access.key'='admin'," +
//                "  'fs.s3a.secret.key'='admin123456'," +
//                "  'fs.s3a.ssl.enabled'='false'," +
//                "  'fs.s3a.aws.credentials.provider'='org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider'," +
//                "  ''=''," +
                "  's3.endpoint'='http://127.0.0.1:9000'," +
                "  's3.access-key-id'='admin'," +
                "  's3.secret-access-key'='admin123456'," +
//                "  'io-impl'='org.apache.iceberg.aws.s3.S3FileIO'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG hadoop_catalog");
    }
}
