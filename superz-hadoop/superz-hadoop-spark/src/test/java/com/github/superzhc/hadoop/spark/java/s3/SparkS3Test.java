package com.github.superzhc.hadoop.spark.java.s3;

import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DataFrameMain;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SparkS3Test {
    private SparkSession spark = null;

    private AKTools akTools;

    @Before
    public void setUp() throws Exception {
        SparkConf conf = new SparkConf()
                .setAppName("S3 Test")
                .setMaster("local");
        AWSMain awsMain = new AWSMain(conf, "127.0.0.1:9000", "admin", "admin123456");
        spark = SparkSession.builder().config(awsMain.getConf()).getOrCreate();
        akTools = new AKTools("127.0.0.1");
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void readParquet() {
        Dataset<Row> df = spark.read().parquet("s3a://superz/spark/202303021603");
        df.show(1000, false);
    }

    // 不能直接读取csv
//    @Test
//    public void readCSV() {
//        Dataset<Row> df = spark.read().csv("s3a://superz/spark/202303021629");
//        df.show(1000, false);
//    }

    @Test
    public void writeParquet() {
        List<Map<String, Object>> data = akTools.get("fund_money_fund_daily_em"/*"fund_scale_close_sina"*/);
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.write().mode(SaveMode.Overwrite).parquet("s3a://superz/spark/s3/parquet_fund_money_fund_daily_em");
    }

    @Test
    public void writeCSV() {
        List<Map<String, Object>> data = akTools.get("fund_money_fund_daily_em"/*"fund_scale_close_sina"*/);
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.write().mode(SaveMode.Overwrite).csv("s3a://superz/spark/s3/csv_fund_money_fund_daily_em");
    }

    @Test
    public void writeJson() {
        List<Map<String, Object>> data = akTools.get("fund_money_fund_daily_em"/*"fund_scale_close_sina"*/);
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.write().mode(SaveMode.Overwrite).json("s3a://superz/spark/s3/json_fund_money_fund_daily_em");
    }
}