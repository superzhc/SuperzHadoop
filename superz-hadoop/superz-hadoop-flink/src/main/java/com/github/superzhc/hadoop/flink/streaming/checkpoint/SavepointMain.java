package com.github.superzhc.hadoop.flink.streaming.checkpoint;

import com.github.superzhc.hadoop.flink.FlinkRestAPI;
import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.streaming.state.StateMain;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/28 11:51
 */
public class SavepointMain {
    /**
     * Savepoint 触发方式有以下三种：
     * 1. 使用 flink savepoint 命令触发 Savepoint，其是在程序运行期间触发 savepoint。
     * 2. 使用 flink cancel -s 命令，取消作业时，并触发 Savepoint。
     * 3. 使用 Rest API 触发 Savepoint，格式为 `jobs/:jobid /savepoints`
     */

    /**
     * 原理：
     * Flink Savepoint 作为实时任务的全局镜像，其在底层使用的代码和 Checkpoint 的代码是一样的，因为 Savepoint 可以看做 Checkpoint在特定时期的一个状态快照。
     */

    public static void main(String[] args) throws Exception {
        Configuration conf = GlobalConfiguration.loadConfiguration(StateMain.class.getResource("/").getPath());
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.setParallelism(1);

        env.enableCheckpointing(3000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        FileSystem.initialize(conf, null);

        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", FakerUtils.Expression.name());
        map.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        map.put("fields.sex.expression", FakerUtils.Expression.options("F", "M"));
        map.put("fields.date.expression", FakerUtils.Expression.expression("date.past", "5", "SECONDS"));
        DataStream<String> ds = env.addSource(new JavaFakerSource(map)).uid("java-faker-source");

        final ObjectMapper mapper = new ObjectMapper();
        ds
                // 不包含状态的算子生成savepoint 【√】
                .map(new MapFunction<String, String>() {
                    @Override
                    public String map(String value) throws Exception {
                        ObjectNode node = (ObjectNode) mapper.readTree(value);
                        node.put("operate_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        return mapper.writeValueAsString(node);
                    }
                })
                // 包含状态的算子生成savepoint 【√】
                // .map(new StateMain.OperatorMapFunctionWithState(mapper))
                .uid("map")
                .print();
        env.execute("savepoint demo");
    }

    static class TriggerSavepoint {
        public static void main(String[] args) throws Exception {
            /* 通过 rest 接口，触发 savepoint */
            String res = new FlinkRestAPI.Job("http://localhost:5064").savepoints("1f3dd166265fc484d33a47d471925063", false, null);
            System.out.println(res);
        }
    }
}
