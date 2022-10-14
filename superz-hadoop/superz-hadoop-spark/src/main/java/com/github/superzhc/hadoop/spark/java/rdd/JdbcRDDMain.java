package com.github.superzhc.hadoop.spark.java.rdd;

import com.github.superzhc.common.jdbc.ResultSetUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.rdd.JdbcRDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * 2020年10月10日 superz add
 */
public class JdbcRDDMain {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("superz").setMaster("local[1]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // 读取mysql数据到RDD
        JavaRDD<List<Map<String, Object>>> rdd = JdbcRDD.create(jsc, new JdbcRDD.ConnectionFactory() {
            @Override
            public Connection getConnection() throws Exception {
                // 加载MySql驱动
                Class.forName("com.mysql.jdbc.Driver");
                // MySQL8.0+驱动
                // Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/news_dw?zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&useSSL=false", "root", "123456");
            }
        }, "select * from mofish_20221011 where iid>=? and iid<?", 1, 2000, 10, new Function<ResultSet, List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call(ResultSet rs) throws Exception {
                return ResultSetUtils.Result2ListMap(rs);
            }
        });

        System.out.println(rdd.count());

        rdd.foreach(d-> System.out.println(d));

        jsc.stop();
    }
}
