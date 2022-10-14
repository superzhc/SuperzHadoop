package com.github.superzhc.hadoop.spark.java.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author superz
 * @create 2022/10/14 15:35
 **/
public class DataFrameMain {
    private static final Logger log= LoggerFactory.getLogger(DataFrameMain.class);

    public static Dataset<Row> rdd2ds(JavaRDD<Map<String,Object>> rdd){
        rdd.map(new Function<Map<String, Object>, Row>() {
            @Override
            public Row call(Map<String, Object> v1) throws Exception {

                return null;
            }
        });

        return null;
    }

    public static void main(String[] args) {

    }
}
