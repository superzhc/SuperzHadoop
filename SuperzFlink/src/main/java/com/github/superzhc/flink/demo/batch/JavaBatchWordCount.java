package com.github.superzhc.flink.demo.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 2020年11月19日 superz add
 */
public class JavaBatchWordCount
{
    public static void main(String[] args) throws Exception {
        // 获取运行环境
        ExecutionEnvironment env=ExecutionEnvironment.getExecutionEnvironment();

        // 获取文件中的内容
        DataSource<String> text=env.readTextFile("D://data/wordcount.txt");
        DataSet<Tuple2<String,Integer>> counts=text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>()
        {
            @Override public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] tokens=value.split("\\W+");
                for(String token:tokens){
                    if(token.length()>0)
                        out.collect(new Tuple2<>(token,1));
                }
            }
        })//
        .groupBy(0)//
        .sum(1)//
        ;

        counts.writeAsCsv("D://data/flink","\n"," ").setParallelism(1);
        env.execute("batch word count");
    }
}
