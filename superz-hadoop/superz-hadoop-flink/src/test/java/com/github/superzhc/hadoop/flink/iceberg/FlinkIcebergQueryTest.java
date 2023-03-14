package com.github.superzhc.hadoop.flink.iceberg;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/15 0:35
 */
public class FlinkIcebergQueryTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() throws Exception {
        // 设置环境变量：HADOOP_CONF_DIR=./target/classes

        Configuration conf = new Configuration();

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        tEnv = StreamTableEnvironment.create(env);

        // 设置Catalog
        String sql = "CREATE CATALOG hadoop_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hadoop'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
    }
}
