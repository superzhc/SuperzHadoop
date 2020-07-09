package com.github.superzhc.spark;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.spark.JavaHBaseContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

/**
 * 2020年07月07日 superz add
 */
public class SparkHBaseDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("spark on hbase");
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext sc = new JavaSparkContext(conf);

        Configuration hconf = HBaseConfiguration.create();
        hconf.set("hbase.zookeeper.quorum", "ep-001.hadoop,ep-002.hadoop,ep-003.hadoop");
        hconf.set("hbase.zookeeper.property.clientPort", "2181");
        JavaHBaseContext hc = new JavaHBaseContext(sc, hconf);

        Scan scan = new Scan();
        JavaRDD<Tuple2<ImmutableBytesWritable, Result>> rdd = hc.hbaseRDD(TableName.valueOf("superz"), scan);
    }
}
