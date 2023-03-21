package com.github.superzhc.hadoop.flink.table;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.Catalog;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author superz
 * @create 2023/3/15 16:58
 **/
public class CatalogTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() throws Exception {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }


    public void jdbcCatalog() throws Exception{
        /**
         * JdbcCatalog 允许用户通过 JDBC 协议将 Flink 连接到关系数据库。
         *
         * 目前，JDBC Catalog 有两个实现，即 Postgres Catalog 和 MySQL Catalog。
         */
        String sql="CREATE CATALOG my_catalog WITH(" +
                "    'type' = 'jdbc'," +
                "    'default-database' = '...'," +
                "    'username' = '...'," +
                "    'password' = '...'," +
                "    'base-url' = '...'" +
                ")";
    }

    @Test
    public void icebergHadoopCatalog() throws Exception {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");

        // 定义Catalog
        String sql = "CREATE CATALOG hadoop_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hadoop'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);

        Catalog catalog = tEnv.getCatalog("hadoop_catalog").get();
        System.out.println(catalog.listDatabases());
    }

    @Test
    public void listCatalogs() {
        System.out.println(Arrays.asList(tEnv.listCatalogs()));
    }

    @Test
    public void listDatabases() {
        System.out.println(Arrays.asList(tEnv.listDatabases()));
    }
}
