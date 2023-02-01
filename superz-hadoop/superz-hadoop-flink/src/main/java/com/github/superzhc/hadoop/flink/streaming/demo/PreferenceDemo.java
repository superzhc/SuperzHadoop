package com.github.superzhc.hadoop.flink.streaming.demo;

import com.github.superzhc.data.news.MoFish;
import com.github.superzhc.hadoop.flink2.streaming.operator.map.DropKeyFunction;
import com.github.superzhc.hadoop.flink2.streaming.operator.map.PatternFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/9/30 14:08
 **/
public class PreferenceDemo {
    private static final Logger log = LoggerFactory.getLogger(PreferenceDemo.class);

    private static final String regex = "<span style=\"color: red\">价格￥(\\d+\\.?\\d*)</span>(\\S*)";
    private static final Pattern pattern = Pattern.compile(regex);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env.addSource(new SourceFunction<Map<String, Object>>() {
                    private volatile boolean cancel = false;

                    @Override
                    public void run(SourceContext<Map<String, Object>> ctx) throws Exception {
                        while (!cancel) {
                            List<Map<String, Object>> lst = MoFish.jingdong();
                            for (Map<String, Object> map : lst) {
                                ctx.collect(map);
                            }

                            Thread.sleep(1000 * 60 * 1);
                        }
                    }

                    @Override
                    public void cancel() {
                        cancel = true;
                    }
                })
//                .map(new DropKeyFunction<String>("Url", "imgUrl"))
//                .map(new PatternFunction("Title", regex, true))
                .print()
        ;

        env.execute("preference demo");
    }
}
