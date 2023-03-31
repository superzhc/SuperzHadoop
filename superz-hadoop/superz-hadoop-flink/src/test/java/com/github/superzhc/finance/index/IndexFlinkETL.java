package com.github.superzhc.finance.index;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.util.HadoopUtils;
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
    static {
        // 全局配置HADOOP配置文件所在路径
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes/s3local");
    }

    StreamExecutionEnvironment env;

    TableEnvironment tEnv;

    @Before
    public void setUp() {
        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoints");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoints");
        conf.setString("s3a.endpoint","http://127.0.0.1:9000");
        conf.setString("s3a.access-key","admin");
        conf.setString("s3a.secret-key","admin123456");
        conf.setString("s3a.path.style.access","true");
        // checkpoint 必须使用如下代码进行初始化
        FileSystem.initialize(conf);


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
                "  'clients'='5'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG iceberg_hive");
        tEnv.executeSql("SHOW DATABASES").print();
    }

    public void indexBasic() {
        String sql = "CREATE TABLE index_basic (" +
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
                "    'warehouse'='s3a://superz/finance'" +
                ")";
        tEnv.executeSql(sql);
    }

    @Test
    public void indexOneMinuteInfo() {
        String kafkaSql = "CREATE TABLE kafka_index_one_minute_info(  " +
                "    `date` DATE," +
                "    code STRING," +
                "    name STRING," +
                "    last_code DOUBLE," +
                "    `open` DOUBLE," +
                "    high DOUBLE," +
                "    low DOUBLE," +
                "    `new` DOUBLE," +
                "    change DOUBLE," +
                "    change_amount DOUBLE," +
                "    volume BIGINT," +
                "    amount BIGINT" +
                ")   " +
                "WITH (  " +
                "    'connector'='kafka',  " +
                "    'topic'='finance_stock_zh_index_spot',  " +
                "    'properties.bootstrap.servers' = '127.0.0.1:19092',  " +
//                "    'properties.group.id' = 'finance_group',  " +
                "    'properties.group.id' = 'test_group',  " +
                "    'scan.startup.mode' = 'earliest-offset',  " +
                "    'format' = 'json',  " +
                "    'json.ignore-parse-errors' = 'true'  " +
                ")";
        tEnv.executeSql(kafkaSql);
        tEnv.executeSql("SELECT * FROM kafka_index_one_minute_info").print();


//        String sql = "CREATE TABLE index_per_minute(" +
//                "  `date` DATE," +
//                "  code STRING," +
//                "  name STRING," +
//                "  last_code DOUBLE," +
//                "  `open` DOUBLE," +
//                "  high DOUBLE," +
//                "  low DOUBLE," +
//                "  new DOUBLE," +
//                "  change DOUBLE," +
//                "  change_amount DOUBLE," +
//                "  volume LONG," +
//                "  amount LONG," +
//                "  date_hour STRING" +
//                "  ,PRIMARY KEY(code,date_hour) NOT ENFORCED" +
//                ") " +
//                "WITH (" +
//                "  'connector'='iceberg'," +
//                "  'catalog-name'='iceberg_hive'," +
//                "  'catalog-type'='hive'," +
//                "  'catalog-database'='finance'," +
//                "  'catalog-table'='index_one_minute_info'," +
//                "  'uri'='thrift://127.0.0.1:9083'," +
//                "  'warehouse'='s3a://superz/finance'" +
//                ")";
//        tEnv.executeSql(sql);
    }
}
