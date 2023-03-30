package com.github.superzhc.finance.index;

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
 * @create 2023/3/29 20:45
 */
public class IndexSparkETL {

    AKTools api = null;
    SparkSession spark = null;

    @Before
    public void setUp() throws Exception {
        api = new AKTools("127.0.0.1", 8080);

        SparkConf sparkConf = new SparkConf()
                .set("spark.sql.legacy.timeParserPolicy", "LEGACY")
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
                .appName("Spark ETL Index")
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
    public void indices() {
        List<Map<String, Object>> indexStockInfoData = api.get("index_stock_info");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, indexStockInfoData, "index_code", "display_name");
        ds.createOrReplaceTempView("index_stock_info");
        spark.sql("INSERT INTO bigdata_hive.finance.index_basic SELECT index_code,display_name,NULL,NULL,to_date(publish_date, 'yyyy-MM-dd'),NULL FROM index_stock_info");
        spark.sql("SELECT * FROM bigdata_hive.finance.index_basic").show();
    }

    // 废弃该方法，使用indices
    // @Test
    public void indices2() {
        List<Map<String, Object>> realTimeIndicesData = api.get("stock_zh_index_spot");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, realTimeIndicesData, "code", "name");
        ds.createOrReplaceTempView("stock_zh_index_spot");
        // insert不支持指定部分列进行插入，即如下写法不支持
        // spark.sql("INSERT INTO bigdata_hive.finance.index_basic(code,name) SELECT code,name FROM stock_zh_index_spot");
        spark.sql("INSERT INTO bigdata_hive.finance.index_basic SELECT code,name,NULL,NULL,NULL,NULL FROM stock_zh_index_spot");

        // 更新数据
        // String sql = "MERGE INTO bigdata_hive.finance.index_basic t " +
        //         "   USING (SELECT code,name FROM stock_zh_index_spot WHERE code IS NOT NULL AND name IS NOT NULL) s" +
        //         "   ON t.code = s.code" +
        //         "   WHEN MATCHED THEN UPDATE SET t.code=s.code,t.name=s.name" +
        //         "   WHEN NOT MATCHED THEN INSERT (code,name) VALUES(s.code,s.name)";
        // spark.sql(sql);

        spark.sql("SELECT * FROM bigdata_hive.finance.index_basic").show();
    }

    @Test
    public void indices3() {
        List<Map<String, Object>> indexStockInfoData = api.get("index_all_cni");
        Dataset<Row> ds = DatasetUtils.fromMap(spark, indexStockInfoData, "code", "name");
        ds.createOrReplaceTempView("index_all_cni");
        String sql = "MERGE INTO bigdata_hive.finance.index_basic t" +
                "   USING (SELECT code,name,sample_number FROM index_all_cni) s" +
                "   ON t.code = s.code" +
                "   WHEN MATCHED THEN UPDATE SET t.sample_number=s.sample_number" +
                // 注意事项：INSERT clauses must provide values for all columns of the target table.
                "   WHEN NOT MATCHED THEN INSERT (code,name,full_name,sample_number,publish_date,description) VALUES(s.code,s.name,NULL,s.sample_number,NULL,NULL)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.index_basic").show();
    }

    //@Test
    public void deleteIndices() {
        spark.sql("DELETE FROM bigdata_hive.finance.index_basic");
        spark.sql("SELECT * FROM bigdata_hive.finance.index_basic").show();
    }

    // @Test
    // public void todayIndexInfo() {
    //     List<Map<String, Object>> indexStockInfoData = api.get("index_all_cni");
    //     Dataset<Row> ds = DatasetUtils.fromMap(spark, indexStockInfoData, "code", "name");
    //     ds.createOrReplaceTempView("index_all_cni");
    //     spark.sql("INSERT INTO bigdata_hive.finance.index_info SELECT current_date(),code,name,NULL,NULL,NULL,close,change,PE_TTM,volume,amount,total_market,circulation_market FROM index_all_cni");
    //     spark.sql("SELECT * FROM bigdata_hive.finance.index_info").show(100);
    // }

    @Test
    public void indexInfo() {
        String code = "sz399552";
        String name = "央视成长";

        // 股票指数的历史数据按日频率更新
        List<Map<String, Object>> data = api.get("stock_zh_index_daily", Collections.singletonMap("symbol", code));
        Dataset<Row> ds = DatasetUtils.fromMap(spark, data, "date");
        ds = ds
                .withColumn("code", lit(code.substring(2)))
                .withColumn("name", lit(name))
                .withColumn("date", to_date(col("date"), "yyyy-MM-dd"))
        ;
        ds.createOrReplaceTempView("stock_zh_index_daily");

        String sql = "MERGE INTO bigdata_hive.finance.index_info t" +
                "   USING (SELECT * FROM stock_zh_index_daily) s" +
                "   ON t.code = s.code AND t.date=s.date" +
                "   WHEN MATCHED THEN UPDATE SET t.open=s.open,t.high=s.high,t.low=s.low,t.close=s.close,t.volume=s.volume" +
                "   WHEN NOT MATCHED THEN INSERT (date,code,name,open,high,low,close,change,pe_ttm,volume,amount,total_market,circulation_market) VALUES(s.date,s.code,s.name,s.open,s.high,s.low,s.close,NULL,NULL,s.volume,NULL,NULL,NULL)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.index_info").show(100);
    }

    @Test
    public void indexInfo2() {
        Map<String, String> map = new HashMap<>();
        map.put("399005", "中小100");

        Dataset<Row> ds2 = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            List<Map<String, Object>> data = api.get("index_hist_cni", Collections.singletonMap("symbol", code));

            Dataset<Row> ds = DatasetUtils.fromMap(spark, data);
            ds = ds
                    .withColumn("date2", to_date(col("date"), "yyyy-MM-dd"))
                    .withColumn("code", lit(code))
                    .withColumn("name", lit(name));

            if (ds2 == null) {
                ds2 = ds;
            } else {
                ds2 = ds2.union(ds);
            }
        }

        ds2.createOrReplaceTempView("index_hist_cni");
        String sql = "MERGE INTO bigdata_hive.finance.index_info t" +
                "   USING (SELECT * FROM index_hist_cni) s" +
                "   ON t.code = s.code AND t.date=s.date2" +
                "   WHEN MATCHED THEN UPDATE SET t.open=s.open,t.high=s.high,t.low=s.low,t.close=s.close,t.change=s.change,t.volume=s.volume,t.amount=s.amount" +
                "   WHEN NOT MATCHED THEN INSERT (date,code,name,open,high,low,close,change,pe_ttm,volume,amount,total_market,circulation_market) VALUES(s.date2,s.code,s.name,s.open,s.high,s.low,s.close,s.change,NULL,s.volume,s.amount,NULL,NULL)";
        spark.sql(sql);
        spark.sql("SELECT * FROM bigdata_hive.finance.index_info").show(100);
    }
}
