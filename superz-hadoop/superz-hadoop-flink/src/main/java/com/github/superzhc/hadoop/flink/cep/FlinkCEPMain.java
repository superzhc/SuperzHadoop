package com.github.superzhc.hadoop.flink.cep;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CEP
 * <p>
 * 复杂事件处理（Complex Event Processing，以下简称CEP）是一种分析事件流的技术，事件可以是事物有意义的状态变化也可以是事物之间的动作。主要用于分析事件之间的关联关系，且这种关联关系在时间上是多种多样的。
 *
 * @author superz
 * @create 2022/3/11 16:34
 **/
public class FlinkCEPMain {
    public static void main(String[] args) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put(JavaFakerSource.field("name"), FakerUtils.Expression.NAME);
        fakerConfigs.put(JavaFakerSource.field("age"), FakerUtils.Expression.age(1, 100));
        fakerConfigs.put(JavaFakerSource.field("edu"), FakerUtils.Expression.EDUCATION);
        DataStream<JsonNode> ds = env.addSource(new JavaFakerSource(fakerConfigs)).map(d -> mapper.readTree(d));

        /**
         * flink CEP 表达式
         * 满足两个条件：
         * 1. age>18 and age<60
         * 2. 学历：专科及以上
         */
        Pattern<JsonNode, JsonNode> pattern = Pattern.<JsonNode>begin("first")
                .where(new IterativeCondition<JsonNode>() {
                    @Override
                    public boolean filter(JsonNode jsonNode, Context<JsonNode> context) throws Exception {
                        int age = jsonNode.get("age").asInt();
                        return age > 18 && age < 60;
                    }
                })
                .next("second")
                .where(new IterativeCondition<JsonNode>() {
                    @Override
                    public boolean filter(JsonNode jsonNode, Context<JsonNode> context) throws Exception {
                        String edu = jsonNode.get("edu").asText();
                        return "专科".equals(edu) || "本科".equals(edu) || "硕士".equals(edu) || "博士".equals(edu);
                    }
                });

        // 将 CEP 表达式运用到流中，筛选数据
        PatternStream<JsonNode> patternStream = CEP.pattern(ds, pattern);
        DataStream<String> ds2=patternStream.select(new PatternSelectFunction<JsonNode, String>() {
            @Override
            public String select(Map<String, List<JsonNode>> pattern) throws Exception {
                return null;
            }
        });

        ds2.print();
        env.execute("cep");
    }
}
