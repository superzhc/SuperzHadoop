package com.github.superzhc.hadoop.spark.java.data;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DataFrameMain;
import com.github.superzhc.hadoop.spark.java.dataframe.DatasetUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

/**
 * @author superz
 * @create 2023/3/6 21:56
 */
public class AKShareTest {
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
                .set("spark.sql.catalog.akshare", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.akshare.type", "hadoop")
                .set("spark.sql.catalog.akshare.warehouse", "s3a://superz/akshare")
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
    public void stock_zh_index_hist_csindex() {
        // spark.sql("CREATE TABLE akshare.spark.stock_zh_index_hist_csindex(date string,code string,name string,open double,close double,high double,low double,change_amount double,change double,volume double) USING iceberg");

        List<Map<String, Object>> data = akTools.get("stock_zh_index_hist_csindex");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data);
        ds.createOrReplaceTempView("t1");

//        ds.printSchema();
        ds.show(10000, false);

        // UPSERT 语句，甚至更灵活进行更多操作
        String sql = "MERGE INTO akshare.spark.stock_zh_index_hist_csindex t USING (SELECT date,code,name,open,close,high,low,change_amount,change,volume FROM t1) u ON t.code = u.code " +
                "WHEN MATCHED THEN UPDATE SET t.date=u.date,t.open=u.open,t.close=u.close,t.high=u.high,t.low=u.low,t.change_amount=u.change_amount,t.change=u.change,t.volume=u.volume " +
                // "WHEN NOT MATCHED THEN INSERT *"
                "WHEN NOT MATCHED THEN INSERT date,code,name,open,close,high,low,change_amount,change,volume values(u.date,u.code,u.name,u.open,u.close,u.high,u.low,u.change_amount,u.change,u.volume)";
        spark.sql(sql);

        spark.table("akshare.spark.stock_zh_index_hist_csindex").show(1000, false);
    }

    @Test
    public void stock_sse_summary() {
        List<Map<String, Object>> data = akTools.get("stock_sse_summary");

        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds = ds.withColumn("date", lit(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).cast(DataTypes.DateType));
        // ds.show();
        ds.writeTo("akshare.stock_sse_summary").create();
    }

    @Test
    public void stock_szse_summary() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        List<Map<String, Object>> data = akTools.get("stock_szse_summary", params);

        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds = ds.withColumn("date", lit(date).cast(DataTypes.DateType));
        // ds.show();
        ds.writeTo("akshare.stock_szse_summary").create();
    }


    @Test
    public void stock_szse_area_summary() throws Exception {
        LocalDate now = LocalDate.now().minusMonths(3);
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMM"));

        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        List<Map<String, Object>> data = akTools.get("stock_szse_area_summary", params);

        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds = ds.withColumn("date", lit(date));
        // ds.show();
        ds.writeTo("akshare.stock_szse_area_summary").append();
    }

    @Test
    public void stock_individual_info_em() {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "600000");
        List<Map<String, Object>> data = akTools.get("stock_individual_info_em", params);

        Map<String, Object> record = new HashMap<>();
        record.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        for (Map<String, Object> item : data) {
            record.put(String.valueOf(item.get("item")), item.get("value"));
        }

        Dataset<Row> ds = DataFrameMain.maps2ds(spark, Collections.singletonList(record));
        ds.createOrReplaceTempView("stock_individual_info_em_origin");
        // spark.sql("CREATE TABLE akshare.stock_individual_info_em USING iceberg AS SELECT * FROM stock_individual_info_em_origin");
        spark.sql("INSERT INTO akshare.stock_individual_info_em SELECT * FROM stock_individual_info_em_origin");
        spark.sql("select * from akshare.stock_individual_info_em").show();
    }

    @Test
    public void stock_zh_a_new() {
        List<Map<String, Object>> data = akTools.get("stock_zh_a_new");

        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds = ds.withColumn("date", lit(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).cast(DataTypes.DateType));
        ds.writeTo("akshare.stock_zh_a_new").createOrReplace();
    }

    @Test
    public void stock_zh_a_alerts_cls() {
        // List<Map<String, Object>> data = akTools.get("stock_zh_a_alerts_cls");
        // Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        // ds.writeTo("akshare.stock_zh_a_alerts_cls").create();

        spark.table("akshare.stock_zh_a_alerts_cls").show(100, false);
    }

    @Test
    public void news_cctv() {
        // List<Map<String, Object>> data = new ArrayList<>();
        // LocalDate start = LocalDate.of(2023, 1, 2);
        // // LocalDate now = LocalDate.now();
        // // while (start.isBefore(now)) {
        //     Map<String, Object> params = new HashMap<>();
        //     params.put("date", start.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        //     data.addAll(akTools.get("news_cctv", params));
        // //     start=start.plusDays(1);
        // // }
        // Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        // ds = ds.withColumn("date2", to_date(col("date"), "yyyyMMdd"));
        // ds.createOrReplaceTempView("t1");
        // spark.sql("INSERT INTO akshare.news_cctv SELECT date2 as date,title,content FROM t1");

        // spark.sql("select * from akshare.news_cctv").show(10000,false);

        spark.table("akshare.news_cctv").printSchema();
    }

    @Test
    public void funds() {
        List<Map<String, Object>> data = akTools.get("fund_rating_all");

        Dataset<Row> ds = DatasetUtils.fromMap(spark, data);
        ds.createOrReplaceTempView("fund_rating_all");
        ds.printSchema();

        spark.sql("CREATE TABLE akshare.fund_basic USING iceberg AS SELECT * FROM fund_rating_all");
        spark.table("akshare.fund_basic").show(100, false);
    }
}
