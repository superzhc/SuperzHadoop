package com.github.superzhc.hadoop.flink.streaming;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维表关联
 *
 * @author superz
 * @create 2021/11/9 17:40
 */
public class JavaDataStreamJoinDimensionMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        final ObjectMapper mapper = new ObjectMapper();

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));

        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        ds
                .print();

        env.execute("javafaker source");
    }

    // region 预加载维表

    /**
     * 通过定义一个类实现 RichFunction 接口，在 open() 中读取维表数据加载到内存中，之后在执行函数中与维表数据进行关联
     * <p>
     * 实现 RichFunction 接口 open() 方法里加载维表数据到内存的方式特点如下：
     * 优点：实现简单
     * 缺点：因为数据存于内存，所以只适合小数据量并且维表数据更新频率不高的情况下。虽然可以在open中定义一个定时器定时更新维表，但是还是存在维表更新不及时的情况。
     */
    public static class DimensionRichMapFunction extends RichMapFunction<String, String> {
        private Map<String, String> dimensionCache;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            // 初始化缓存
            dimensionCache = new ConcurrentHashMap<>();

            // 给维表赋值，维表的数据可以从内存、文件、数据库、接口中去获取
            dimensionCache.put("0", "v0");
            dimensionCache.put("1", "v1");
        }

        @Override
        public String map(String value) throws Exception {
            // use dimensionCache join
            return value;
        }
    }
    // endregion

    // region 热存储维表
    /**
     * 热存储维表
     *
     * 将维表数据存储在Redis、HBase、MySQL等外部存储中，实时流在关联维表数据的时候实时去外部存储中查询
     *
     * 这种方式特点如下：
     * 优点：维度数据量不受内存限制，可以存储很大的数据量。
     * 缺点：因为维表数据在外部存储中，读取速度受制于外部存储的读取速度；另外维表的同步也有延迟。
     */

    /* 方案一：使用缓存来减少对外部存储的请求，同预加载维表的模式 */
    /* 方案二：使用异步 IO 来提高访问吞吐量，待研究 */
    // endregion

    // region 广播维表
    /**
     * 广播维表
     *
     * 利用Flink的Broadcast State将维度数据流广播到下游做join操作。
     *
     * 特点如下：
     * 优点：维度数据变更后可以即时更新到结果中。
     * 缺点：数据保存在内存中，支持的维度数据量比较小。
     */
    // endregion

    // region Temporal table function join
    // endregion
}
