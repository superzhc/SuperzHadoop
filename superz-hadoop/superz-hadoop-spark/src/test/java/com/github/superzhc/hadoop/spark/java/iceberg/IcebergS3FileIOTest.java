package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DataFrameMain;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/6 9:25
 **/
public class IcebergS3FileIOTest {
    private AKTools akTools;
    private SparkSession spark = null;

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
                // iceberg S3FileIO 配置
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.test", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.test.type", "hadoop")
                .set("spark.sql.catalog.test.io-impl", "org.apache.iceberg.aws.s3.S3FileIO")
//                .set("spark.sql.catalog.demo.s3.endpoint","http://127.0.0.1:9000")
                .set("spark.sql.catalog.test.warehouse", "s3a://superz/iceberg2")
        ;
        spark = SparkSession.builder().config(conf).getOrCreate();
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

//    @Test
    public void write1() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "20230301");

        List<Map<String, Object>> data = akTools.get("bond_cash_summary_sse", params);

        Dataset<Row> ds= DataFrameMain.maps2ds(spark,data);

        ds.writeTo("test.bond_cash_summary_sse").createOrReplace();
    }
}
