package com.github.superzhc.hadoop.flink.streaming.operator;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * “富函数”
 * DataStream API 提供的一个函数类接口，所有 Flink 函数类都有其 Rich 版本；富函数可以获取运行环境的上下文，并拥有一些声明周期。
 * <p>
 * RichFunction 有一个生命周期概念，典型的生命周期方法有：
 * 1. `open()`：这是初始化方法，在算子的处理方法调用之前被调用
 * 2. `close()`
 * 3. `getRuntimeContext()`：提供了函数的 RuntimeContext 的一些信息
 *
 * @author superz
 * @create 2021/10/28 9:50
 */
public class RichFunctionMain {

    /**
     * 富函数的生命周期
     */
    static class RichFunctionLifeCycle extends RichMapFunction<String, String> {

        private ObjectMapper mapper;
        private String initFlag;

        public RichFunctionLifeCycle(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        /**
         * 初始化操作
         *
         * @param parameters
         * @throws Exception
         */
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            initFlag = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        @Override
        public String map(String value) throws Exception {
            // 并行度
            int parallel = getRuntimeContext().getNumberOfParallelSubtasks();
            String taskName = getRuntimeContext().getTaskName();
            String taskNameWithSub = getRuntimeContext().getTaskNameWithSubtasks();

            ObjectNode node = (ObjectNode) mapper.readTree(value);
            node.put("parallel", parallel);
            node.put("taskName", taskName);
            node.put("taskNameWithSub", taskNameWithSub);
            node.put("richFunctionInitFlag", initFlag);
            return mapper.writeValueAsString(node);
        }

        @Override
        public void close() throws Exception {
            super.close();
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", FakerUtils.Expression.name());
        map.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        map.put("fields.sex.expression", FakerUtils.Expression.options("F", "M"));
        map.put("fields.multi_operator.expression", FakerUtils.Expression.options("张三", "lisi", "王五", "zhaoliu"));
        map.put("fields.date.expression", FakerUtils.Expression.expression("date.past", "5", "SECONDS"));
        DataStream<String> ds = env.addSource(new JavaFakerSource(map));

        final ObjectMapper mapper = new ObjectMapper();
        ds.map(new RichFunctionLifeCycle(mapper)).print();

        env.execute("rich function");
    }
}
