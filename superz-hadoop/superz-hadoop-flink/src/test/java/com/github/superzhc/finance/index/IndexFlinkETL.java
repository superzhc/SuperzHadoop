package com.github.superzhc.finance.index;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/30 23:32
 */
public class IndexFlinkETL {
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
                "  'warehouse'='s3a://superz/finance'," +
                "  'hadoop-conf-dir'='./target/classes'," +
                "  'clients'='5'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG iceberg_hive");
        tEnv.executeSql("SHOW DATABASES").print();
    }

    public void indexBasic(){
        String sql="CREATE TABLE index_basic (" +
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
                // "    'hadoop-conf-dir'='./target/classes'," +
                "    'warehouse'='s3a://superz/finance'" +
                ")";
        tEnv.executeSql(sql);
    }
}
