package com.github.superzhc.hadoop.flink.streaming.optimization;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据倾斜
 *
 * @author superz
 * @create 2021/11/25 16:31
 */
public class DataSkews {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String,String> fakerConfigs=new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression",FakerUtils.Expression.age(18,60));
        fakerConfigs.put("fields.type.expression",FakerUtils.Expression.numberBetween(1,20));
        fakerConfigs.put("fields.salary.expression",FakerUtils.Expression.randomDouble(2,5000,100000));
        DataStream<String> ds=env.addSource(new JavaFakerSource(fakerConfigs));

        final ObjectMapper mapper=new ObjectMapper();

        // 先打散

        ds.print();

        env.execute("data skews");
    }
}
