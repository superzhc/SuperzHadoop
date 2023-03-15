package com.github.superzhc.hadoop.flink.iceberg;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
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
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");
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
                "    id   BIGINT," +
                "    data STRING" +
                ") WITH (" +
                "    'connector'='iceberg'," +
                "    'catalog-type'='hadoop'," +
                "    'catalog-name'='hadoop'," +
                "    'catalog-database'='test'," +
                "    'catalog-table'='t1'," +
                "    'warehouse'='s3a://superz/flink/iceberg'" +
                ")";
        tEnv.executeSql(sql);

        tEnv.sqlQuery("SELECT * FROM flink_table").execute().print();
    }
}
