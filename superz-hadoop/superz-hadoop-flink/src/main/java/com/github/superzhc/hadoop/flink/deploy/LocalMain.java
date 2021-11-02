package com.github.superzhc.hadoop.flink.deploy;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/11/1 13:41
 */
@Deprecated
public class LocalMain {

    /**
     * Local Mode 执行 StreamGraph 任务
     */


    public static void main(String[] args) throws Exception {
        ParameterTool params=ParameterTool.fromArgs(args);
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params);

        Map<String,String> map=new HashMap<>();
        map.put("name", FakerUtils.Expression.name());
        map.put("age",FakerUtils.Expression.age(1,80));
        DataStream<String> ds=env.addSource(new JavaFakerSource(map));

        ds.print();

        env.execute("local mode demo");
    }
}
