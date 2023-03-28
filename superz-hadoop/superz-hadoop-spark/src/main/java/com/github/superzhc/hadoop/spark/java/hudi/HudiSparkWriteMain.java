package com.github.superzhc.hadoop.spark.java.hudi;

import com.github.superzhc.data.fund.EastMoneyFund;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/27 10:29
 **/
public class HudiSparkWriteMain {
    public static void main(String[] args) {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        SparkConf conf = new SparkConf()
                .setAppName("hudi spark")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.hudi.catalog.HoodieCatalog")
                .set("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")
                .setMaster("local");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext sc = JavaSparkContext.fromSparkContext(spark.sparkContext());

        List<Map<String, Object>> data = EastMoneyFund.fundNetHistory("000478");

        data=new ArrayList<>();
        for (int i=1;i<200;i++){
            Map<String,Object> item=new HashMap<>();
            item.put("id",i);
            item.put("order_id",i*2.0);
            data.add(item);
        }

        JavaRDD<Row> rdd = sc.parallelize(data).map(new Function<Map<String, Object>, Row>() {
            @Override
            public Row call(Map<String, Object> map) throws Exception {
                Seq<Object> seq = JavaConverters.collectionAsScalaIterableConverter(map.values()).asScala().toSeq();
//                 return Row.fromSeq(seq);
                return null;
            }
        });

        StructField id = new StructField("id", DataTypes.IntegerType, false, Metadata.empty());
        StructField orderId = new StructField("order_id", DataTypes.FloatType, false, Metadata.empty());
        StructField code = new StructField("code", DataTypes.StringType, false, Metadata.empty());
        StructField date = new StructField("date", DataTypes.StringType, false, Metadata.empty());
        StructField netWorth = new StructField("net_worth", DataTypes.StringType, false, Metadata.empty());
        StructField accumulatedNetWorth = new StructField("accumulated_net_worth", DataTypes.StringType, false, Metadata.empty());
        StructField change = new StructField("change", DataTypes.StringType, false, Metadata.empty());
        StructType schema = new StructType(new StructField[]{id,orderId/*code, date, netWorth, accumulatedNetWorth, change*/});
        Dataset<Row> ds = spark.createDataFrame(rdd, schema);

        ds.show(false);

        /*ds.write()
                .format("hudi")
                // 插入数据用 OverWrite，更新数据用 Append
                .mode(SaveMode.Overwrite)
                .option(DataSourceWriteOptions.OPERATION().key(), DataSourceWriteOptions.BULK_INSERT_OPERATION_OPT_VAL()) // 定义写操作类型。值可以为upsert，insert，bulk_insert和delete，默认值为upsert
                .option(HoodieWriteConfig.PRECOMBINE_FIELD_NAME.key(), "ts")                 // 预合并字段
                .option(HoodieWriteConfig.KEYGENERATOR_CLASS_NAME.key(), ComplexKeyGenerator.class.getName())
                .option(KeyGeneratorOptions.RECORDKEY_FIELD_NAME.key(), "id")                // 主键字段
                .option(KeyGeneratorOptions.PARTITIONPATH_FIELD_NAME.key(), "code")          // 分区字段
                .option(HoodieWriteConfig.TBL_NAME.key(), "superz_hudi_spark_client")  // 表名
                .save("");
        ;*/

        spark.stop();
    }
}
