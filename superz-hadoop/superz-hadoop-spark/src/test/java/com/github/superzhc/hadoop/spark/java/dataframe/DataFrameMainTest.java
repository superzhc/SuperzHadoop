package com.github.superzhc.hadoop.spark.java.dataframe;

import com.github.superzhc.data.other.AKTools;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DataFrameMainTest {

    SparkSession spark = null;
    AKTools akTools;

    @Before
    public void setUp() throws Exception {
        SparkConf conf = new SparkConf()
                .setAppName("DataFrame Test")
                .setMaster("local");
        spark = SparkSession.builder().config(conf).getOrCreate();
        akTools = new AKTools("127.0.0.1", 8080);
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void testMap2rdd() {
        List<Map<String, Object>> data = akTools.get("fund_scale_close_sina");
        Dataset<Row> ds = DataFrameMain.maps2ds(spark, data);
        ds.show(10000, false);
    }
}