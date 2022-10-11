package com.github.superzhc.hadoop.spark.java;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.Map;

/**
 * Java 版，广播变量
 *
 * @author superz
 * @create 2022/10/11 14:20
 **/
public class BroadcastMain {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .getOrCreate();

        String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false";
        String username = "root";
        String password = "123456";

        List<Map<String, Object>> myConfigs = null;
        List<Map<String, Object>> data = null;
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            myConfigs = jdbc.query("select * from my_configs");
            data = jdbc.query("select * from xgit_spider limit 10");
        }

        // 将数据广播出去，注意：不要在Java代码中直接用 SparkContext 进行广播
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        final Broadcast<List<Map<String, Object>>> myConfigsBD = jsc.broadcast(myConfigs);

        jsc.parallelize(data).foreach(
                row -> System.out.println(myConfigsBD.value())
        );

        spark.stop();
    }
}
