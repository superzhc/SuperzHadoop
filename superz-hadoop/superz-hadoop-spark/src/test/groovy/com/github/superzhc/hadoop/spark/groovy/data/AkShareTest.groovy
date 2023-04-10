package com.github.superzhc.hadoop.spark.groovy.data

import com.github.superzhc.data.other.AKTools
import com.github.superzhc.hadoop.spark.java.dataframe.DatasetUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static org.apache.spark.sql.functions.lit

/**
 * @author superz
 * @create 2023/4/10 9:54
 * */
class AkShareTest {
    private SparkSession spark = null;

    private AKTools akTools;

    @Before
    void setUp() throws Exception {
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
                .set("spark.sql.catalog.akshare", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.akshare.type", "hadoop")
                .set("spark.sql.catalog.akshare.warehouse", "s3a://superz/akshare")
        spark = SparkSession.builder().config(conf).getOrCreate()
    }

    @After
    void tearDown() throws Exception {
        if (null != spark) {
            spark.stop()
        }
    }

    @Test
    void stock_sse_summary() {
        List<Map<String, Object>> data = akTools.get("stock_sse_summary")

//        String[] notNullableFields = ["code"]
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data)
        ds = ds.withColumn("date", lit(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).cast(DataTypes.DateType))
        ds.show()
    }
}
