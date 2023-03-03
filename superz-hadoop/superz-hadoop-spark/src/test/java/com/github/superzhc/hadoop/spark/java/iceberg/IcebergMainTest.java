package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DataFrameMain;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.*;
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
    public void createTablePartition() {
        String code = "513500";
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", code);
        params.put("period", "daily");
        params.put("start_date", "19900101");
        params.put("end_date", "20230302");
        // params.put("adjust","");

        List<Map<String, Object>> data = akTools.get("fund_etf_hist_em", params);
//        // 新增一列常数列
//        MapUtils.constant("code", code, data);
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        // 新增一列
        ds.withColumn("code", functions.lit(code));
//        ds.select(ds.col("code"))
        ds = ds.selectExpr("code"
                //, "to_unix_timestamp(`日期`,'yyyy-MM-dd') as ts"
                , "to_timestamp(to_date(`日期`,'yyyy-MM-dd')) as ts"
                , "`开盘` as open"
                , "`收盘` as close"
                , "`最高` as high"
                , "`最低` as low"
        );
        ds.createOrReplaceTempView("fundsHistory");
//        spark.sql("select * from fundsHistory").show();

        spark.sql("CREATE TABLE test.akshare.fund_etf_hist_em USING iceberg PARTITIONED BY (months(ts),code) AS SELECT * FROM fundsHistory");
    }

    @Test
    public void testCTAS() {
        List<Map<String, Object>> data = akTools.get("fund_name_em");
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.createOrReplaceTempView("funds");

        spark.sql("CREATE TABLE test.akshare.fund_name_em USING iceberg AS SELECT * FROM funds");
        spark.sql("select * from test.akshare.fund_name_em").show(1000, false);
    }

    @Test
    public void write0() {
        String sql = "INSERT INTO test.iceberg_db.sample VALUES (1, 'a'), (2, 'b'), (3, 'c')";
        spark.sql(sql);
    }

    @Test
    public void write1() {
        List<Map<String, Object>> data = akTools.get("fund_money_fund_daily_em");
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.printSchema();
        ds.writeTo("test.akshare.fund_money_fund_daily_em").createOrReplace();
    }

    @Test
    public void write2() {
        String code = "513500";
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", code);
        params.put("period", "daily");
        params.put("start_date", "19900101");
        params.put("end_date", "20230302");
        // params.put("adjust","");

        List<Map<String, Object>> data = akTools.get("fund_etf_hist_em", params);
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        // 新增一列
        ds = ds.withColumn("code", functions.lit(code))
                .withColumn("ts", to_timestamp(to_date(col("日期"), "yyyy-MM-dd")))
        ;
//        ds.show(1000, false);
        ds.createOrReplaceTempView("fundsHistory");

        spark.sql("INSERT INTO test.akshare.fund_etf_hist_em SELECT code,ts,`开盘` as open,`最高` as high,`最低` as low,`收盘` as close FROM fundsHistory");
        spark.sql("select * from test.akshare.fund_etf_hist_em").show(1000, false);
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
        Dataset<Row> ds = spark.read().format("iceberg").load("test.akshare.fund_name_em");
        ds.show(10000, false);
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