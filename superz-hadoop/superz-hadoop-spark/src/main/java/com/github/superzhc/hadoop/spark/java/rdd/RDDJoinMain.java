package com.github.superzhc.hadoop.spark.java.rdd;

import com.github.superzhc.data.news.MoFish;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RDD join操作
 *
 * @author superz
 * @create 2022/10/12 13:38
 **/
public class RDDJoinMain {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("rdd join")
                .master("local")
                .getOrCreate();
        SparkContext sc = spark.sparkContext();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<Map<String, Object>> typesRdd = jsc.parallelize(MoFish.allTypes());
        JavaPairRDD<String, Map<String, Object>> typesRdd2 = typesRdd
                .keyBy(new Function<Map<String, Object>, String>() {
                    @Override
                    public String call(Map<String, Object> v1) throws Exception {
                        return (String) v1.get("type");
                    }
                })
                .mapToPair(new PairFunction<Tuple2<String, Map<String, Object>>, String, Map<String, Object>>() {
                    @Override
                    public Tuple2<String, Map<String, Object>> call(Tuple2<String, Map<String, Object>> stringMapTuple2) throws Exception {
                        Map<String, Object> value = stringMapTuple2._2;
                        value.remove("icon");
                        value.remove("img");
                        return stringMapTuple2;
                    }
                });

        List<Map<String, Object>> data = new ArrayList<>();
        data.addAll(MoFish.acFun());
        data.addAll(MoFish.bilibili());
        JavaRDD<Map<String, Object>> dataRdd = jsc.parallelize(data);

        JavaPairRDD<String, Map<String, Object>> dataRdd2 = dataRdd.keyBy(new Function<Map<String, Object>, String>() {
            @Override
            public String call(Map<String, Object> v1) throws Exception {
                return (String) v1.get("TypeName");
            }
        });

        join(typesRdd2, dataRdd2);

        spark.stop();
    }

    /**
     * 类似SQL的inner join操作，返回结果是前面和后面配对成功的，过滤掉关联不上的。
     *
     * @param rdd1
     * @param rdd2
     */
    static void join(JavaPairRDD<String, Map<String, Object>> rdd1, JavaPairRDD<String, Map<String, Object>> rdd2) {
        JavaPairRDD<String, Tuple2<Map<String, Object>, Map<String, Object>>> rdd = rdd1.<Map<String, Object>>join(rdd2);
        System.out.println("数据条数：" + rdd.count());
        rdd.foreach(item -> {
            String key = item._1();
            Map<String, Object> v1 = item._2._1;
            Map<String, Object> v2 = item._2._2;
            System.out.println("==============分割线=========================");
            System.out.println(key);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println("==============分割线=========================");
        });
    }

    /**
     * leftOuterJoin类似于SQL中的左外关联left outer join，返回结果以前面的RDD为主，关联不上的记录为空。只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
     *
     * @param rdd1
     * @param rdd2
     */
    static void leftOuterJoin(JavaPairRDD<String, Map<String, Object>> rdd1, JavaPairRDD<String, Map<String, Object>> rdd2) {
        JavaPairRDD<String, Tuple2<Map<String, Object>, Optional<Map<String, Object>>>> rdd = rdd1.<Map<String, Object>>leftOuterJoin(rdd2);
        System.out.println("数据条数：" + rdd.count());
        rdd.foreach(item -> {
            String key = item._1();
            Map<String, Object> v1 = item._2._1;
            Map<String, Object> v2 = item._2._2.orElse(null);
            System.out.println("==============分割线=========================");
            System.out.println(key);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println("==============分割线=========================");
        });
    }

    static void rightOuterJoin(JavaPairRDD<String, Map<String, Object>> rdd1, JavaPairRDD<String, Map<String, Object>> rdd2) {
        JavaPairRDD<String, Tuple2<Optional<Map<String, Object>>, Map<String, Object>>> rdd = rdd1.<Map<String, Object>>rightOuterJoin(rdd2);
        System.out.println("数据条数：" + rdd.count());
        rdd.foreach(item -> {
            String key = item._1();
            Map<String, Object> v1 = item._2._1.orElse(null);
            Map<String, Object> v2 = item._2._2;
            System.out.println("==============分割线=========================");
            System.out.println(key);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println("==============分割线=========================");
        });
    }

    static void fullOuterJoin(JavaPairRDD<String, Map<String, Object>> rdd1, JavaPairRDD<String, Map<String, Object>> rdd2) {
        JavaPairRDD<String, Tuple2<Iterable<Map<String, Object>>, Iterable<Map<String, Object>>>> rdd = rdd1.<Map<String, Object>>cogroup(rdd2);
        System.out.println("数据条数：" + rdd.count());
        rdd.foreach(item -> {
            String key = item._1();
        });
    }
}
