package com.github.superzhc.finance.fund;

import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DatasetUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.*;

/**
 * @author superz
 * @create 2023/3/30 0:01
 */
public class FundSparkETL {
    AKTools api = null;
    SparkSession spark = null;

    @Before
    public void setUp() throws Exception {
        api = new AKTools("127.0.0.1", 8080);

        SparkConf sparkConf = new SparkConf()
                .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.iceberg.check-nullability", "false")
                .set("spark.sql.catalog.bigdata_hive", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.bigdata_hive.type", "hive")
                .set("spark.sql.catalog.bigdata_hive.uri", "thrift://127.0.0.1:9083")
                .set("spark.sql.catalog.bigdata_hive.warehouse", "s3a://superz/finance")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.access.key", "admin")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        spark = SparkSession.builder()
                .appName("Spark ETL Fund")
                .master("local")
                .config(sparkConf)
                .getOrCreate();
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void funds() {
        List<Map<String, Object>> data = api.get("fund_name_em");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data, "code", "name");
        ds.createOrReplaceTempView("fund_name_em");
        spark.sql("INSERT INTO bigdata_hive.finance.fund_basic SELECT code,name,NULL,type,NULL,NULL FROM fund_name_em");
        spark.sql("SELECT * FROM bigdata_hive.finance.fund_basic").show();
    }

    @Test
    public void funds2() {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "全部");
        params.put("indicator", "全部");

        List<Map<String, Object>> data = api.get("fund_info_index_em", params);
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data, "code", "name");
        ds.createOrReplaceTempView("fund_info_index_em");

        String sql = "MERGE INTO bigdata_hive.finance.fund_basic t" +
                "   USING(SELECT * FROM fund_info_index_em) s" +
                "   ON t.code=s.code" +
                "   WHEN MATCHED THEN UPDATE SET t.index=s.index,t.indicator=s.indicator" +
                "   WHEN NOT MATCHED THEN INSERT (code,name,full_name,type,index,indicator) VALUES(s.code,s.name,NULL,NULL,s.index,s.indicator)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.fund_basic").show();
    }

    @Test
    public void fundsInfo() {
        List<Map<String, Object>> data = api.get("fund_etf_spot_em");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data, "code", "name");
        ds.createOrReplaceTempView("fund_etf_spot_em");

        String sql = "MERGE INTO bigdata_hive.finance.fund_info t" +
                "   USING(SELECT * FROM fund_etf_spot_em) s" +
                "   ON t.code=s.code AND t.date=current_date()" +
                "   WHEN MATCHED THEN UPDATE SET t.new=s.new,t.change_amount=s.change_amount,t.change=s.change,t.volume=s.volume,t.amount=s.amount,t.open=s.open,t.high=s.high,t.low=s.low,t.last_close=s.last_close,t.turnover=s.turnover,t.circulation_market=s.circulation_market,t.total_market=s.total_market" +
                "   WHEN NOT MATCHED THEN INSERT (date,code,name,new,last_close,open,high,low,close,change_amount,change,volume,amount,turnover,circulation_market,total_market) VALUES(current_date(),s.code,s.name,s.new,s.last_close,s.open,s.high,s.low,NULL,s.change_amount,s.change,s.volume,s.amount,s.turnover,s.circulation_market,s.total_market)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.fund_info").show();
    }

    @Test
    public void fundsInfo2() {
        // "封闭式基金", "ETF基金", "LOF基金"
        String type = "ETF基金";
        List<Map<String, Object>> data = api.get("fund_etf_category_sina", Collections.singletonMap("symbol", type));
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data, "code", "name");
        ds = ds
                .withColumn("code", substring(col("code"), 3, 6))
        ;
        ds.createOrReplaceTempView("fund_etf_category_sina");

        String sql = "MERGE INTO bigdata_hive.finance.fund_info t" +
                "   USING(SELECT * FROM fund_etf_category_sina) s" +
                "   ON t.code=s.code AND t.date=current_date()" +
                "   WHEN MATCHED THEN UPDATE SET t.new=s.new,t.change_amount=s.change_amount,t.change=s.change,t.volume=s.volume,t.amount=s.amount,t.open=s.open,t.high=s.high,t.low=s.low,t.last_close=s.last_close" +
                "   WHEN NOT MATCHED THEN INSERT (date,code,name,new,last_close,open,high,low,close,change_amount,change,volume,amount,turnover,circulation_market,total_market) VALUES(current_date(),s.code,s.name,s.new,s.last_close,s.open,s.high,s.low,NULL,s.change_amount,s.change,s.volume,s.amount,NULL,NULL,NULL)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.fund_info").show();
    }
}
