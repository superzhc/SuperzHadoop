package com.github.superzhc.hadoop.flink1_15.finance.index;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2023/4/1 13:41
 **/
public class IndexFlinkETL {
    public static void main(String[] args) throws Exception {
        SystemUtils.setEnv("HADOOP_CONF_DIR", IndexFlinkETL.class.getClass().getResource("/").getPath() + "s3local");

        Configuration conf = new Configuration();
        conf.setBoolean("table.dynamic-table-options.enabled", true);
        conf.setBoolean("table.exec.iceberg.use-flip27-source", true);
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoints");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoints");
        conf.setString("s3a.endpoint", "http://127.0.0.1:9000");
        conf.setString("s3a.access-key", "admin");
        conf.setString("s3a.secret-key", "admin123456");
        conf.setString("s3a.path.style.access", "true");
        // checkpoint 必须使用如下代码进行初始化
        FileSystem.initialize(conf);


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.enableCheckpointing(1000);
        env.setParallelism(1);

        TableEnvironment tEnv = StreamTableEnvironment.create(env);

        String kafkaSql = "CREATE TABLE kafka_index_one_minute_info(  " +
                "    `date` STRING," +
                "    code STRING," +
                "    name STRING," +
                "    last_close DOUBLE," +
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
                "    'properties.group.id' = 'finance_group2',  " +
                "    'scan.startup.mode' = 'earliest-offset',  " +
                "    'format' = 'json'," +
                "    'json.ignore-parse-errors' = 'true'  " +
                ")";
        tEnv.executeSql(kafkaSql);
//        tEnv.executeSql("SELECT * FROM kafka_index_one_minute_info").print();


        String icebergSql = "CREATE TABLE index_per_minute(" +
                // 注意flink类型和iceberg类型，当我设置位数为3的时候，获取时间报错，也就无法写入数据
                "  `date` TIMESTAMP(6)," +
                "  code STRING," +
                "  name STRING," +
                "  last_close DOUBLE," +
                "  `open` DOUBLE," +
                "  high DOUBLE," +
                "  low DOUBLE," +
                "  `new` DOUBLE," +
                "  `change` DOUBLE," +
                "  change_amount DOUBLE," +
                "  volume BIGINT," +
                "  amount BIGINT," +
                "  date_hour STRING" +// 分区字段
                "  ,PRIMARY KEY(code,`date`,date_hour) NOT ENFORCED" +
                ") " +
                "WITH (" +
                "  'connector'='iceberg'," +
                "  'catalog-name'='iceberg_hive'," +
                "  'catalog-type'='hive'," +
                "  'catalog-database'='finance'," +
                "  'catalog-table'='index_one_minute_info'," +
                "  'uri'='thrift://127.0.0.1:9083'," +
                "  'warehouse'='s3a://superz/finance'" +
                ")";
        tEnv.executeSql(icebergSql);

        String sql = "INSERT INTO index_per_minute" +
                "   SELECT" +
                "       TO_TIMESTAMP(`date`)," +
                "       code,name,last_close,`open`,high,low,`new`,change,change_amount,volume,amount," +
                "       DATE_FORMAT(TO_TIMESTAMP(`date`),'yyyy-MM-dd')" +
                "   FROM kafka_index_one_minute_info";
        tEnv.executeSql(sql);
        // tEnv.executeSql("SELECT * FROM index_per_minute").print();
    }
}
