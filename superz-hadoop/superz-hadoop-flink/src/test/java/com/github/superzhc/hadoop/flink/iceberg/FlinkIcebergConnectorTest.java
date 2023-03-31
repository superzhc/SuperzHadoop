package com.github.superzhc.hadoop.flink.iceberg;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.iceberg.flink.actions.Actions;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/15 9:45
 **/
public class FlinkIcebergConnectorTest {

    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    static {
        // 设置环境变量：HADOOP_CONF_DIR=./target/classes
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes/s3local");
    }

    @Before
    public void setUp() throws Exception {
        Configuration conf = new Configuration();

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        tEnv = StreamTableEnvironment.create(env);
    }

    @Test
    public void table() {
        String sql = "CREATE TABLE flink_table(" +
                "   title string," +
                "   description string," +
                "   guid string," +
                "   link string," +
                "   sourceType string," +
                "   syncDate TIMESTAMP(3)," +
                "   rsshubKey string," +
                "   pubDate TIMESTAMP(3)," +
                // 设置主键，若需要支持upsert功能，主键必须进行设置
                "   PRIMARY KEY(guid,rsshubKey) NOT ENFORCED" +
                ") WITH (" +
                "    'connector'='iceberg'," +
                "    'catalog-type'='hadoop'," +
                "    'catalog-name'='hadoop'," +
                "    'catalog-database'='rsshub'," +
                "    'catalog-table'='shopping'," +
                "    'warehouse'='s3a://superz/flink/iceberg'" +
                ")";
        tEnv.executeSql(sql);

        tEnv.sqlQuery("SELECT * FROM flink_table").execute().print();
    }

}
