package com.github.superzhc.hadoop.flink;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerParallelSource;
import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * 分区策略
 *
 * @author superz
 * @create 2021/11/4 11:16
 */
public class PartitionerMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> fakerConfig = new HashMap<>();
        fakerConfig.put("fields.name.expression", "#{Name.name}");
        fakerConfig.put("fields.age.expression", "#{number.number_between '1','80'}");
        fakerConfig.put("fields.date.expression", FakerUtils.Expression.pastDate(10));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfig));
        DataStream<String> parallelDs = env.addSource(new JavaFakerParallelSource(fakerConfig, 2L));

        // ds.print();

        final ObjectMapper mapper = new ObjectMapper();
        DataStream<String> partitionDs = parallelDs.rebalance();
        partitionDs.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                ObjectNode node = (ObjectNode) mapper.readTree(value);
                node.put("flag", "partition operator");
                return mapper.writeValueAsString(node);
            }
        }).print();

        env.execute("partition main");
    }

    /**
     * 数据会被分发到下游算子的第一个实例中进行处理。
     * <p>
     * 由源码可知，selectChannel 方法返回 0，即只发送给下游算子的第一个 task。
     */
    public static class GlobalPartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            return ds.global();
        }
    }

    /**
     * 数据会被随机分发到下游算子的每一个实例中进行。
     */
    public static class ShufflePartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            return ds.shuffle();
        }
    }

    /**
     * 数据会被循环发送到下游的每一个实例中进行处理。
     */
    public static class RebalancePartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            return ds.rebalance();
        }
    }

    /**
     * 这种分区器会根据上下游算子的并行度，循环的方式输出到下游算子的每个实例。这里有点难以理解，假设上游并行度为 2，编号为 A 和 B。下游并行度为 4，编号为 1，2，3，4。那么 A 则把数据循环发送给 1 和 2，B 则把数据循环发送给 3 和 4。假设上游并行度为 4，编号为 A，B，C，D。下游并行度为 2，编号为 1，2。那么 A 和 B 则把数据发送给 1，C 和 D 则把数据发送给 2。
     */
    public static class RescalePartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            return ds.rescale();
        }
    }

    /**
     * 广播分区会将上游数据输出到下游算子的每个实例中。适合于大数据集和小数据集做Jion的场景。
     */
    public static class BroadcastPartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            // 广播
            return ds.broadcast();
        }
    }

    /**
     * 用于将记录输出到下游本地的算子实例。它要求上下游算子并行度一样。简单的说，ForwardPartitioner用来做数据的控制台打印。
     */
    public static class ForwardPartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            // 上下游并行度一样时一对一发送
            return ds.forward();
        }
    }

    /**
     * Hash 分区器。会将数据按Key的Hash值输出到下游算子实例中。
     */
    public static class KeyGroupStreamPartitioner {
//        public static <T> DataStream<T> partition(DataStream<T> ds) {
//            return ds;
//        }
    }

    /**
     * 用户自定义分区器
     */
    public static class CustomPartitioner {
        public static <T> DataStream<T> partition(DataStream<T> ds) {
            return ds.partitionCustom(new Partitioner<T>() {
                @Override
                public int partition(T key, int numPartitions) {
                    return 0;
                }
            }, new KeySelector<T, T>() {
                @Override
                public T getKey(T value) throws Exception {
                    return value;
                }
            });
        }
    }
}
