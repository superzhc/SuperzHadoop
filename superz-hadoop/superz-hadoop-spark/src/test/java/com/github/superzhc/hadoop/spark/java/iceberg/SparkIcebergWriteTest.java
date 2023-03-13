package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DatasetUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/13 14:39
 **/
public class SparkIcebergWriteTest {
    AKTools akTools;

    SparkSession spark;

    @Before
    public void setUp() throws Exception {
        akTools = new AKTools("127.0.0.1");

        SparkConf conf = new SparkConf();
        conf.setAppName("Spark Iceberg Test")
                .setMaster("local")
                // s3 配置
                .set("spark.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.hadoop.fs.s3a.access.key", "admin")
                .set("spark.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                // iceberg 配置
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.test", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.test.type", "hadoop")
                .set("spark.sql.catalog.test.warehouse", "s3a://superz/demo")
                .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
        ;
        spark = SparkSession.builder().config(conf).getOrCreate();
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void insertInto() {
        spark.sql("INSERT INTO test.spark.t1 VALUES (11, '2023-03-13'), (12, '2023-03-13')");

        spark.table("test.spark.t1").show(1000, false);
    }

    @Test
    public void insertIntoSelect() {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("data", LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            data.add(map);
        }

        Dataset<Row> ds = DatasetUtils.fromMap(spark, data);
        ds.createOrReplaceTempView("t_temp");

        spark.sql("INSERT INTO test.spark.t1 SELECT id,data from t_temp");

        spark.table("test.spark.t1").show(1000, false);
    }

    @Test
    public void mergeInto() {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i % 4 == 0 ? (20 + i) : i);
            map.put("data", LocalDate.now().plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put("op", i % 3 == 0 ? "delete" : "update");
            data.add(map);
        }

        Dataset<Row> ds = DatasetUtils.fromMap(spark, data);
        ds.createOrReplaceTempView("t_temp");
        ds.printSchema();

        String sql = "MERGE INTO test.spark.t1 t " +
                "USING (SELECT id,data,op from t_temp) s " +
                "ON t.id = s.id " +
                "WHEN MATCHED AND s.op = 'delete' THEN DELETE " +
                "WHEN MATCHED AND s.op = 'update' THEN UPDATE SET t.data=s.data " +
                "WHEN NOT MATCHED THEN INSERT (id,data) VALUES (s.id,s.data)";
        spark.sql(sql);

        spark.table("test.spark.t1").show(1000, false);
    }

    public void insertOverwrite() {
        // TODO
    }

    @Test
    public void delete(){
        String sql="DELETE FROM test.spark.t1 WHERE id=6";
        spark.sql(sql);

        spark.table("test.spark.t1").show();
    }

    @Test
    public void update(){
        String sql="UPDATE test.spark.t1 SET data='2023-04-13' WHERE id=8";
        spark.sql(sql);

        spark.table("test.spark.t1").show();
    }
}
