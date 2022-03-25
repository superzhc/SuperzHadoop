package com.github.superzhc.hadoop.spark;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

import java.util.Arrays;

/**
 * 2020年10月10日 superz add
 */
public class AccumulatorDemo
{
    public static void main(String[] args) {
        JavaSparkContext jsc = SparkContextUtils.local();

        LongAccumulator accum = jsc.sc().longAccumulator("Example Accumulator");
        System.out.println("初始值：" + accum.value());
        jsc.parallelize(Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9 })).foreach(x -> accum.add(1));
        System.out.println("最终值：" + accum.value());
    }
}
