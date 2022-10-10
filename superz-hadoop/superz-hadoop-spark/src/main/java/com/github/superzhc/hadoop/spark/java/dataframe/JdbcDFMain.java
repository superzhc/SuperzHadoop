package com.github.superzhc.hadoop.spark.java.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author superz
 * @create 2022/10/9 15:08
 **/
public class JdbcDFMain {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .getOrCreate();

        String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");

        /* 表名读取 */
        // Dataset<Row> df = spark.read().jdbc(url, "device_status_result", properties);
        /* 通过创建子表来过滤数据读取 */
        String sql = "(select * from device_status_result where begin_time > '2022-10-10 00:00:00') as t1";
        Dataset<Row> df = spark.read().jdbc(url, sql, properties);

        // System.out.println(df.count());
        // df.show(100);

        JavaRDD<Row> rdd = df.toJavaRDD();
        rdd
                .groupBy(new Function<Row, String>() {
                    @Override
                    public String call(Row v1) throws Exception {
                        return v1.getAs("device_id");
                    }
                })
        // 考虑：先过滤一部分字段数据会不会更好点
//                .map(row ->
//                        {
//                            String startCol = "begin_time";
//                            String endCol = "end_time";
//
//                            Integer startColIndex = row.fieldIndex(startCol);
//                            Integer endColIndex = row.fieldIndex(endCol);
//
//                            // 判断结束时间是否为空，若为空则取当前的整点时间
//                            if (row.isNullAt(endColIndex)) {
//                            }
//
//                            // 计算结束时间和开始时间的时间差，单位为分钟
//                            long l = (row.getTimestamp(endColIndex).getTime() - row.getTimestamp(startColIndex).getTime()) / (1000 * 60);
//
//                            Map<String,Object> ret=new HashMap<>();
//                            ret.put("diff",l);
//
//                            return ret;
//                        }
//                )
        ;


        spark.stop();
    }
}
