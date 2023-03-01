package com.github.superzhc.hadoop.spark.java.hudi;

import com.github.superzhc.data.fund.EastMoneyFund;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
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
 * @create 2022/12/30 13:38
 **/
public class HudiSparkSQLWriteMain {
    public static void main(String[] args) {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        SparkConf conf = new SparkConf()
                .setAppName("hudi spark")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.hudi.catalog.HoodieCatalog")
                .set("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")
                .setMaster("local");

        SparkSession spark = SparkSession.builder()
                .config(conf)
                /*开启hive*/
                .enableHiveSupport()
                //设置hive元数据连接地址
//                .config("hive.metastore.uris", "thrift://hanyun-3:9083")
                .config("hive.metastore.uris", "thrift://10.90.18.83:9083")
                .getOrCreate();
        JavaSparkContext sc = JavaSparkContext.fromSparkContext(spark.sparkContext());

        /*创建表*/
        String ddlSQL = "";
//        // 以下sql可成功创建表，并指定了hudi表的路径  √
//        ddlSQL="create table superz_spark_client_20221230150601( " +
//                " id bigint,date string,a double,b double,ts bigint" +
//                " ) using hudi" +
//                " tblproperties (type = 'cow',primaryKey = 'id',preCombineField = 'ts') " +
//                " partitioned by (code string) " +
//                " location 'hdfs://hanyun-3:8020/hudi/superz/superz_spark_client_20221230150601'";
//        spark.sql(ddlSQL);
//        spark.sql("select * from superz_spark_client_20221230142601").show();

//        // SparkSQL创建的表是hudi已存在的表  √
//        ddlSQL = "create table superz_spark_client_20221230152653 using hudi location 'hdfs://hanyun-3:8020/hudi/superz/superz_java_client_20221230152653'";
//        spark.sql(ddlSQL);

//        // CTAS，未验证该场景
//        ddlSQL="create table superz_spark_client_20221230154901 using hudi tblproperties (primaryKey = 'id') as select 1 as id,'a1' as name, 10 as price";

//        spark.sql("select * from superz_spark_client_20221230150601").show();

        /*插入数据*/
//        String dmlSQL = "insert into superz_java_client_20221230173512 " +
//                "select '2' as id,'2022-12-30' as date,'000002' as code,1.0 as a,1.0 as b,1672388185 as ts";
//        spark.sql(dmlSQL);

//        spark.sql("use xgit_gctest;");
//            spark.sql("show tables;").show();

        List<Map<String, Object>> data =new ArrayList<>();
        for (int i=11;i<2000;i++){
            Map<String,Object> item=new HashMap<>();
            item.put("id",i);
            Double d=(i*2.0);
            item.put("order_id",d.floatValue());
            data.add(item);
        }

        JavaRDD<Row> rdd = sc.parallelize(data).map(new Function<Map<String, Object>, Row>() {
            @Override
            public Row call(Map<String, Object> map) throws Exception {
//                Seq<Object> seq = JavaConverters.collectionAsScalaIterableConverter(map.values()).asScala().toSeq();
//                 return Row.fromSeq(seq);
                Object[] objs=map.values().toArray();
                return RowFactory.create(objs);
            }
        });

        StructField id = new StructField("id", DataTypes.IntegerType, false, Metadata.empty());
        StructField orderId = new StructField("order_id", DataTypes.FloatType, false, Metadata.empty());
        StructType schema = new StructType(new StructField[]{id,orderId/*code, date, netWorth, accumulatedNetWorth, change*/});
        Dataset<Row> ds = spark.createDataFrame(rdd, schema);
        ds.createOrReplaceTempView("test1");

        String databaseName="xgit_zuhuadmin";
        String tableName = "zbx3";
        tableName="mm";
//        spark.sql("use xgit_gctest");
        spark.sql("use "+databaseName);
        spark.sql("desc " + tableName).show();
//        spark.sql("insert into " + tableName + " select '1' as id,'2023-01-03' as d,1.0 as a,1.0 as b,1672712334 as ts,'000001' as c");
//        for(int i=11;i<200;i++) {
//            spark.sql("insert into " + tableName + "  select " + i + " as id," + (i * 2.0) + " as order_id");
//        }
//        spark.sql("insert into "+tableName+" select id,order_id from test1");
        spark.sql("select * from " + tableName).show(1000);

//        spark.sql("insert into student select 1 as id,'xxx' as name,10 as age");
//        spark.sql("insert into student select 1,'yyyy',20");

//        spark.sql("select * from superz_spark_client_20221230152653");


//        JavaSparkContext sc = JavaSparkContext.fromSparkContext(spark.sparkContext());
//
//        List<Map<String, Object>> data = EastMoneyFund.fundNetHistory("012348");
//        JavaRDD<Row> rdd = sc.parallelize(data).map(new Function<Map<String, Object>, Row>() {
//            @Override
//            public Row call(Map<String, Object> map) throws Exception {
//                Seq<Object> seq = JavaConverters.collectionAsScalaIterableConverter(map.values()).asScala().toSeq();
//                return Row.fromSeq(seq);
//            }
//        });
//
//        StructField code = new StructField("code", DataTypes.StringType, false, Metadata.empty());
//        StructField date = new StructField("date", DataTypes.StringType, false, Metadata.empty());
//        StructField netWorth = new StructField("net_worth", DataTypes.StringType, false, Metadata.empty());
//        StructField accumulatedNetWorth = new StructField("accumulated_net_worth", DataTypes.StringType, false, Metadata.empty());
//        StructField change = new StructField("change", DataTypes.StringType, false, Metadata.empty());
//        StructType schema = new StructType(new StructField[]{code, date, netWorth, accumulatedNetWorth, change});
//        Dataset<Row> ds = spark.createDataFrame(rdd, schema);
//
//        ds.show(100, false);

        spark.stop();
    }
}
