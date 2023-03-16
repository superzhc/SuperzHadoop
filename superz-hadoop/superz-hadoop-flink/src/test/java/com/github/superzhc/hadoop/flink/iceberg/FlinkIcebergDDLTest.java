package com.github.superzhc.hadoop.flink.iceberg;

import com.github.superzhc.common.utils.SystemUtils;
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

    static {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");
    }

    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() throws Exception {
        // 设置环境变量：HADOOP_CONF_DIR=./target/classes

        Configuration conf = new Configuration();
        // conf.setString("fs.allowed-fallback-filesystems", "s3");
        // conf.setString("s3a.endpoint", "http://127.0.0.1:9000");
        // conf.setBoolean("s3a.path.style.access", true);
        // conf.setString("s3a.access-key", "admin");
        // conf.setString("s3a.secret-key", "admin123456");
        // conf.setBoolean("s3a.ssl.enabled", false);
        // conf.setString("s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
        // // conf.setString("s3a.impl","org.apache.hadoop.fs.s3a.S3AFileSystem");
        // FileSystem.initialize(conf, null);

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
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG hadoop_catalog");
    }

    @Test
    public void createDatabase() {
        createHadoopCatalog();

        tEnv.executeSql("CREATE DATABASE test");
        tEnv.executeSql("USE test");
    }

    @Test
    public void createTable() {
        createHadoopCatalog();

        String sql = "CREATE TABLE `hadoop_catalog`.`test`.`t1` (" +
                "    id BIGINT COMMENT 'unique id'," +
                "    data STRING" +
                ")";
        tEnv.executeSql(sql);
    }

    @Test
    public void createTableLike() {
        createHadoopCatalog();

        String sql = "CREATE TABLE datagen (\n" +
                " f_random INT,\n" +
                " f_random_str STRING\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second'='5',\n" +
                " 'fields.f_random.min'='1',\n" +
                " 'fields.f_random.max'='1000',\n" +
                " 'fields.f_random_str.length'='10'\n" +
                ")";
        tEnv.executeSql(sql);

        sql = "CREATE TABLE  `hadoop_catalog`.`test`.`t_202303161029` LIKE `datagen`";
        tEnv.executeSql(sql);

        tEnv.executeSql("SELECT * FROM test.t_202303161029").print();
    }

    public void alterTable() {
        createHadoopCatalog();

        String sql = "ALTER TABLE `hadoop_catalog`.`test`.`t1` SET ('write.format.default'='avro')";
    }

    public void alterTableRenameTo() {
        createHadoopCatalog();

        String sql = "ALTER TABLE `hadoop_catalog`.`test`.`t1` RENAME TO `hadoop_catalog`.`test`.`new_t1`";
    }

    public void dropTable() {
        createHadoopCatalog();

        String sql = "DROP TABLE `hadoop_catalog`.`test`.`t1`";
        tEnv.executeSql(sql);
    }
}
