package com.github.superzhc.hadoop.spark.java.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author superz
 * @create 2022/10/9 15:08
 **/
public class JdbcDFMain {
    private static final Logger log = LoggerFactory.getLogger(JdbcDFMain.class);

    /**
     * 读Jdbc数据源
     */
    static Dataset<Row> read(SparkSession spark) {
        String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");

        /* 表名读取 */
        // Dataset<Row> df = spark.read().jdbc(url, "device_status_result", properties);
        /* 通过创建子表来过滤数据读取 */
        String sql = "(select * from device_status_result where begin_time > '2022-10-10 00:00:00') as t1";
        Dataset<Row> df = spark.read().jdbc(url, sql, properties);
        return df;
    }


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .getOrCreate();

        Dataset<Row> df = read(spark);

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
                .map(new Function<Tuple2<String, Iterable<Row>>, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> call(Tuple2<String, Iterable<Row>> v1) throws Exception {
                        String startCol = "begin_time";
                        String endCol = "end_time";

                        double stopTotal = 0.0;
                        double runningTotal = 0.0;
                        double standbyTotal = 0.0;
                        double faultTotal = 0.0;

                        String key = v1._1;
                        Iterable<Row> data = v1._2;
                        for (Row item : data) {
                            Timestamp startTs = item.getAs(startCol);
                            Timestamp endTs = item.getAs(endCol);

                            double diff = (endTs.getTime() - startTs.getTime()) / (1000 * 60.0);

                            String deviceStatus = item.getAs("device_status");
                            switch (deviceStatus) {
                                case "运行":
                                    runningTotal += diff;
                                    break;
                                case "故障":
                                    faultTotal += diff;
                                    break;
                                case "待机":
                                    standbyTotal += diff;
                                    break;
                                case "停机":
                                default:
                                    stopTotal += diff;
                                    break;
                            }
                        }

                        log.info("设备【{}】运行时间：{}；故障时间：{}；待机时间：{}；停机时间：{}", key, runningTotal, faultTotal, standbyTotal, stopTotal);

                        // 时间稼动率 = 运行时间 / 负荷时间
                        double loadTotal = (runningTotal + faultTotal + standbyTotal);
                        double sjjdl = loadTotal == 0.0 ? 0.0 : runningTotal / loadTotal;

                        return new Tuple2<>(key, sjjdl);
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
                //.foreach(d -> System.out.printf("设备【%s】的时间稼动率：%f\n", d._1, d._2));
        ;


        spark.stop();
    }
}
