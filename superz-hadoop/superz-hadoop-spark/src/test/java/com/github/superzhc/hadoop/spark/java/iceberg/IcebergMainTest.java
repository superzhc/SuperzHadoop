package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.data.other.AKTools;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IcebergMainTest {
    private SparkSession spark = null;

    private AKTools akTools;

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
                .set("spark.sql.catalog.test.warehouse", "s3a://superz/iceberg")
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
    public void createDB() {
        spark.sql("CREATE DATABASE IF NOT EXISTS test.iceberg_db");
    }

    @Test
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS test.iceberg_db.sample(id BIGINT COMMENT 'unique id', data STRING)USING iceberg";
        spark.sql(sql);
    }

    @Test
    public void write0() {
        String sql = "INSERT INTO test.iceberg_db.sample VALUES (1, 'a'), (2, 'b'), (3, 'c')";
        spark.sql(sql);
    }

    @Test
    public void write() throws Exception {
        List<Row> rows = new ArrayList<>();
        rows.add(RowFactory.create("100", "2015-01-01", "2015-01-01T13:51:39.340396Z"));
        rows.add(RowFactory.create("101", "2015-01-01", "2015-01-01T12:14:58.597216Z"));
        rows.add(RowFactory.create("102", "2015-01-01", "2015-01-01T13:51:40.417052Z"));
        rows.add(RowFactory.create("103", "2015-01-01", "2015-01-01T13:51:40.519832Z"));

        StructField[] fields = new StructField[]{
                DataTypes.createStructField("id", DataTypes.StringType, true),
                DataTypes.createStructField("creation_date", DataTypes.StringType, true),
                DataTypes.createStructField("last_update_time", DataTypes.StringType, true)
        };
        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> ds = spark.createDataFrame(rows, schema);

        spark.sql("CREATE TABLE IF NOT EXISTS test.iceberg_table (id string,creation_date string,last_update_time string) USING iceberg location 's3a://superz/iceberg/iceberg_table'");

        ds.writeTo("test.iceberg_table").append();
    }

    @Test
    public void read() {
        Dataset<Row> ds = spark.read().format("iceberg").load("test.iceberg_table");
        ds.show();
    }

    @Test
    public void read0() {
        Dataset<Row> ds1 = spark.sql("SELECT * FROM test.iceberg_db.sample");
        ds1.show();

        Dataset<Row> ds2 = spark.sql("SELECT count(1) AS count, data FROM test.iceberg_db.sample GROUP BY data");
        ds2.show();
    }

    @Test
    public void update() {
        spark.sql("UPDATE test.iceberg_db.sample SET data = 'x' WHERE id = 3");
        Dataset<Row> ds = spark.sql("select * from test.iceberg_db.sample");
        ds.show();
    }

    @Test
    public void delete() {
        spark.sql("DELETE FROM test.iceberg_db.sample WHERE id = 2");
        Dataset<Row> ds = spark.sql("select * from test.iceberg_db.sample");
        ds.show();
    }
}