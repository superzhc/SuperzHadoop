package com.github.superzhc.hadoop.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.JdbcRDD;
import scala.Function1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 2020年10月10日 superz add
 */
public class JdbcRDDDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("superz").setMaster("local[1]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<Integer> rdd = JdbcRDD.create(jsc, new JdbcRDD.ConnectionFactory()
        {
            @Override
            public Connection getConnection() throws Exception {
                // 加载MySql驱动
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/superz", "root", "123456");
            }
        }, "select * from onetab where id>=? and id<=?", 1, 3789, 10, new Function<ResultSet, Integer>()
        {
            @Override
            public Integer call(ResultSet rs) throws Exception {
                int num = 0;
                while (rs.next()) {
                    num++;
                }
                System.out.println("记录数：" + num);
                return num;
            }
        });
        System.out.println(rdd.count());
    }
}
