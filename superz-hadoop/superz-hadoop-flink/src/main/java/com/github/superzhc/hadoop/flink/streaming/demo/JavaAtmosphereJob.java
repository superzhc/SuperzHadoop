package com.github.superzhc.hadoop.flink.streaming.demo;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/11/13 15:59
 */
public class JavaAtmosphereJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        fakerConfigs.put("fields.rain.state.expression", FakerUtils.Expression.options("晴", "多云", "小雨", "中雨", "大雨"));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        final ObjectMapper mapper = new ObjectMapper();
        ds.map(new MapFunction<String, Tuple3<String, Date, String>>() {
                    @Override
                    public Tuple3<String, Date, String> map(String value) throws Exception {
                        JsonNode node = mapper.readTree(value);
                        // 固定设定一个设备id
                        return Tuple3.of("1", FakerUtils.toDate(node.get("date").asText()), node.get("rain.state").asText());
                    }
                })
                .keyBy(0)
                .process(new KeyedProcessFunction<Tuple, Tuple3<String, Date, String>, Tuple3<String, Date, Date>>() {
                    private transient ValueState<Tuple3<String, Date, Date>> rainState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        ValueStateDescriptor<Tuple3<String, Date, Date>> rainStateDescriptor = new ValueStateDescriptor<Tuple3<String, Date, Date>>("rain-state", TypeInformation.of(new TypeHint<Tuple3<String, Date, Date>>() {
                        }));
                        rainState = getRuntimeContext().getState(rainStateDescriptor);
                    }

                    @Override
                    public void processElement(Tuple3<String, Date, String> value, KeyedProcessFunction<Tuple, Tuple3<String, Date, String>, Tuple3<String, Date, Date>>.Context ctx, Collector<Tuple3<String, Date, Date>> out) throws Exception {
                        // System.out.println("原始数据"+value);
                        Tuple3<String, Date, Date> t3 = rainState.value();
                        if (null == t3) {
                            rainState.update(Tuple3.of(value.f2, value.f1, value.f1));
                            return;
                        }

                        if (!t3.f0.equals(value.f2)) {
                            rainState.update(Tuple3.of(value.f2, value.f1, value.f1));
                        } else {
                            Tuple3<String, Date, Date> t = Tuple3.of(t3.f0, t3.f1, value.f1);
                            rainState.update(t);
                            out.collect(t);
                        }


                    }
                })
                .print()
        ;

        env.execute("atmosphere job");
    }
}
