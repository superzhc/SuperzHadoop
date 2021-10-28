package com.github.superzhc.hadoop.flink.streaming.state;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import com.github.superzhc.hadoop.flink.utils.SimpleCache;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 有状态的计算是流处理框架要实现的重要功能，因为稍复杂的流处理场景都需要记录状态，然后在新流入数据的基础上不断更新状态。
 * <p>
 * 因为 Flink 的一个算子有多个子任务，每个子任务分布在不同实例上，可以把状态理解为某个算子的一个子任务实例上的一个变量，变量记录了数据流的历史；当新数据流入时，可以使用该历史变量来计算。
 * 实际上，Flink 的状态是由算子的子任务来创建和管理的。
 * 一个状态的更新和获取流程是一个算子子任务接收输入流，获取对应的状态，根据新的计算结果更新状态。
 * <p>
 * Flink 有两种基本类型的状态：
 * 1. 托管状态（Managed State）：由 Flink 管理存储、恢复和优化
 * 2. 原生状态（Raw State）：由开发者自行管理，需要用户自己序列化
 * <p>
 * 根据算子有无 Keyed（键），可以分为
 * 1. Keyed State
 * 2. Operator State
 * <p>
 * 注意事项：
 * 1. 无论是 Keyed State 还是 Operator State，Flink 的状态都是基于本地的，即每个算子子任务维护着这个算子子任务对应的状态存储，算子子任务之间的状态不能相互访问
 *
 * @author superz
 * @create 2021/10/12 10:02
 */
public class StateMain {
    /**
     * 状态的使用：
     * 首先要注册一个 StateDescriptor，StateDescriptor 是状态的一种描述，它描述了状态的名称和状态的数据结构。
     * 状态的名称可以用来区分不同的状态，一个算子内可以有多个不同的状态，每个状态的 StateDescriptor 需要设置不同的名字。
     * 同时，也需要指定状态的具体数据机构，Flink 要对其进行序列化和反序列化，以便进行 Checkpoint 和数据恢复工作。
     * <p>
     * 注意事项：
     * 1. 需要在继承 RichFunction 接口的算子中定义
     */

    /**
     * 状态从本质上来说，是 Flink 算子子任务的一种本地数据，为了保证数据可靠性（即失败可恢复），使用 Checkpoint 机制来将状态数据持久化到存储空间上。
     * 状态相关的主要逻辑有两项：
     * 1. 将算子子任务的本地内存数据在 Checkpoint 时 snapshot 写入存储；
     * 2. 初始化或重启应用时，以一定的逻辑从存储中读出并变为算子子任务的本地内存数据。
     */

    /**
     * Keyed State 对上述两项内容做了更完善的封装，开发者可以开箱即用。
     */
    static class KeyedState {
        /**
         * ValueState<T> 是单一变量的状态，T 是某种具体的数据类型，比如 Double、String，或用户自己定义的复杂数据结构。
         * 用户可以使用value()方法获取状态，使用 update(T value)更新状态。
         *
         * @param <T>
         * @return
         */
        public <T> ValueState<T> valueState(RichFunction function, Class<T> clazz) {
            ValueStateDescriptor<T> stateDescriptor = new ValueStateDescriptor<T>("value-state-descriptor", clazz);
            return function.getRuntimeContext().getState(stateDescriptor);
        }

        /**
         * MapState<K,V> 是一个键值对状态。
         * 1. get(key: K)可以获取某个key下的value
         * 2. put(key: K, value: V)可以对某个key设置value
         * 3. contains(key: K)判断某个key是否存在
         * 4. remove(key: K)删除某个key以及对应的value
         * 5. entries(): java.lang.Iterable[java.util.Map.Entry[K, V]]返回MapState中所有的元素
         * 6. iterator(): java.util.Iterator[java.util.Map.Entry[K, V]]返回一个迭代器
         * <p>
         * 需要注意的是，MapState中的key和Keyed State的key不是同一个key。
         *
         * @param <K>
         * @param <V>
         * @return
         */
        public <K, V> MapState<K, V> mapState(RichFunction function, Class<K> kClass, Class<V> vClass) {
            MapStateDescriptor<K, V> stateDescriptor = new MapStateDescriptor<K, V>("map-state-descriptor", kClass, vClass);
            return function.getRuntimeContext().getMapState(stateDescriptor);
        }

        /**
         * ListState[T] 存储了一个由T类型数据组成的列表
         * 1. 使用add(value: T)或addAll(values: java.util.List[T])向状态中添加元素
         * 2. 使用get(): java.lang.Iterable[T]获取整个列表
         * 3. 使用update(values: java.util.List[T])来更新列表，新的列表将替换旧的列表
         *
         * @param <T>
         * @return
         */
        public <T> ListState<T> listState(RichFunction function, Class<T> clazz) {
            ListStateDescriptor<T> stateDescriptor = new ListStateDescriptor<T>("list-state-descriptor", clazz);
            return function.getRuntimeContext().getListState(stateDescriptor);
        }

        /**
         * ReducingState[T] 只有一个元素，而不是一个列表。它的原理是新元素通过add(value: T)加入后，与已有的状态元素使用ReduceFunction合并为一个元素，并更新到状态里。
         *
         * @param <T>
         * @return
         */
        public <T> ReducingState<T> reducingState(RichFunction function, Class<T> clazz) {
            ReducingStateDescriptor<T> stateDescriptor = new ReducingStateDescriptor<T>("reducing-state-descriptor", new ReduceFunction<T>() {
                @Override
                public T reduce(T value1, T value2) throws Exception {
                    return null;
                }
            }, clazz);
            return function.getRuntimeContext().getReducingState(stateDescriptor);
        }

        /**
         * AggregatingState[IN, OUT]与ReducingState[T]类似，也只有一个元素，只不过AggregatingState[IN, OUT]的输入和输出类型可以不一样。
         *
         * @param <In>
         * @param <Out>
         * @return
         */
        public <In, Out> AggregatingState<In, Out> aggregatingState(RichFunction function, Class<In> inClass, Class<Out> outClass) {
            /* 此处只是演示了如何定义 AggregatingState，并无实质意义，注意中间的类型是可以为任意的中间态类型 */
            AggregatingStateDescriptor<In, Out, Out> stateDescriptor = new AggregatingStateDescriptor<In, Out, Out>("aggregating-state-descriptor", new AggregateFunction<In, Out, Out>() {
                @Override
                public Out createAccumulator() {
                    return null;
                }

                @Override
                public Out add(In value, Out accumulator) {
                    return null;
                }

                @Override
                public Out getResult(Out accumulator) {
                    return null;
                }

                @Override
                public Out merge(Out a, Out b) {
                    return null;
                }
            }, outClass);
            return function.getRuntimeContext().getAggregatingState(stateDescriptor);
        }
    }

    /**
     * 对于 Operator State，每个算子子任务管理自己的 Operateor State，或者说每个算子子任务上的数据流共享，可以访问和修改该状态。
     * Flink 的算子子任务上的数据在程序重启、横向伸缩等场景下不能保证百分百的一致性。换句话说，重启 Flink 应用后，某个数据流元素不一定会和上次一样，还能流入该算子子任务上。
     * 因此，需要根据自己的业务场景来设计snapshot和restore的逻辑。为了实现这两个步骤，Flink提供了最为基础的CheckpointedFunction接口类。
     */
    static class OperatorState {
        /**
         * 这种状态以一个列表的形式序列化并存储，以适应横向扩展时状态重分布的问题
         *
         * @param <T>
         * @return
         */
        public <T> ListState<T> listState() {
            new CheckpointedFunction() {
                private static final String CACHE_NAME = "operator_cache";
                private transient ListState<String> listState;

                /**
                 * Checkpoint时会调用这个方法，要实现具体的snapshot逻辑，比如将哪些本地状态持久化
                 * @param context
                 * @throws Exception
                 */
                @Override
                public void snapshotState(FunctionSnapshotContext context) throws Exception {
                    // 将之前的 checkpoint 清理掉
                    listState.clear();
                    // 将最新的写入状态中
                    listState.add((String) SimpleCache.get(CACHE_NAME));
                }

                /**
                 * 初始化时会调用这个方法，向本地状态中填充数据
                 * @param context
                 * @throws Exception
                 */
                @Override
                public void initializeState(FunctionInitializationContext context) throws Exception {
                    ListStateDescriptor<String> listStateDescriptor = new ListStateDescriptor<String>("operator count", String.class);
                    listState = context.getOperatorStateStore().getListState(listStateDescriptor);

                    /* 注意：如果作业重启，listState 中是会存在数据的，这个数据可以填充本地缓存 */
                    Iterable<String> itr = listState.get();
                    for (String str : itr) {
                        SimpleCache.set(CACHE_NAME, str);
                    }
                }
            };

            return null;
        }

//        public <T> UnionListState<T> unionListState(){
//            return null;
//        }

        public <K, V> BroadcastState<K, V> broadcastState() {
            return null;
        }
    }

    /**
     * 状态管理器：
     * <p>
     * 默认情况下，所有的状态都存储在 JVM 的堆内存中，在状态数据过多的情况下，这种方式很有可能导致内存溢出，因此 Flink 该提供了其它方式来存储状态数据，这些存储方式统一称为状态后端 (或状态管理器)。
     * <p>
     * 状态管理器主要有以下三种：
     * 1. MemoryStateBackend：默认的方式，即基于 JVM 的堆内存进行存储，主要适用于本地开发和调试。
     * 2. FsStateBackend：基于文件系统进行存储，可以是本地文件系统，也可以是 HDFS 等分布式文件系统。需要注意而是虽然选择使用了 FsStateBackend ，但正在进行的数据仍然是存储在 TaskManager 的内存中的，只有在 checkpoint 时，才会将状态快照写入到指定文件系统上。
     * 3. RocksDBStateBackend：RocksDBStateBackend 是 Flink 内置的第三方状态管理器，采用嵌入式的 key-value 型数据库 RocksDB 来存储正在进行的数据。等到 checkpoint 时，再将其中的数据持久化到指定的文件系统中，所以采用 RocksDBStateBackend 时也需要配置持久化存储的文件系统。之所以这样做是因为 RocksDB 作为嵌入式数据库安全性比较低，但比起全文件系统的方式，其读取速率更快；比起全内存的方式，其存储空间更大，因此它是一种比较均衡的方案。
     */
    static class StateManager {
        /**
         * 配置方式
         * <p>
         * Flink 支持使用两种方式来配置后端管理器：
         * 1. 基于代码方式进行配置，只对当前作业生效
         * 2. 基于 flink-conf.yaml 配置文件的方式进行配置，对所有部署在该集群上的作业都生效
         */

        void setStateBackend(StreamExecutionEnvironment env) {
            // 配置 FsStateBackend
            env.setStateBackend(new FsStateBackend("hdfs://namenode:40010/flink/checkpoints"));
            // 配置 RocksDBStateBackend
            /* 配置 RocksDBStateBackend 时，需要额外导入下面的依赖
            *
              <dependency>
                <groupId>org.apache.flink</groupId>
                <artifactId>flink-statebackend-rocksdb_2.11</artifactId>
                <version>1.12.0</version>
              </dependency>
            *  */
            //env.setStateBackend(new RocksDBStateBackend("hdfs://namenode:40010/flink/checkpoints"));
        }

        void flinkConfYaml() {
            /**
             * state.backend: filesystem
             * state.checkpoints.dir: hdfs://namenode:40010/flink/checkpoints
             */
        }
    }

    // region 状态的使用

    /**
     * 注意：此函数只用于 Keyed Stream，因为算子内部的状态都是使用的 Keyed State
     */
    public static class KeyedMapFunctionWithState extends RichMapFunction<String, String> {
        private transient ValueState<String> valueState;
        private transient MapState<String, Integer> mapState;
        private transient ListState<String> listState;
        private transient ValueState<Long> totalState;
        private transient ValueState<Long> distinctState;

        private ObjectMapper mapper;

        public KeyedMapFunctionWithState(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            /* 初始化状态 */
            ValueStateDescriptor<String> valueStateDescriptor = new ValueStateDescriptor<>("key append", String.class);
            valueState = getRuntimeContext().getState(valueStateDescriptor);

            MapStateDescriptor<String, Integer> mapStateDescriptor = new MapStateDescriptor<>("key counter", String.class, Integer.class);
            mapState = getRuntimeContext().getMapState(mapStateDescriptor);

            ListStateDescriptor<String> listStateDescriptor = new ListStateDescriptor<>("key list", String.class);
            listState = getRuntimeContext().getListState(listStateDescriptor);

            ValueStateDescriptor<Long> totalStateDescriptor = new ValueStateDescriptor<>("key total user", Long.class);
            totalState = getRuntimeContext().getState(totalStateDescriptor);

            ValueStateDescriptor<Long> distinctStateDescriptor = new ValueStateDescriptor<>("key distinct", Long.class);
            distinctState = getRuntimeContext().getState(distinctStateDescriptor);
        }

        @Override
        public String map(String value) throws Exception {
            ObjectNode node = (ObjectNode) mapper.readTree(value);
            String name = node.get("name").asText();
            String sex = node.get("sex").asText();
            /* 状态操作 */
            // 获取状态值
            String str = valueState.value();
            str = ((null == str) ? "" : str + ",") + name;
            // 更新状态值
            valueState.update(str);

            Long total = (null == totalState.value() ? 0L : totalState.value()) + 1;
            totalState.update(total);

            Long distinct = (null == distinctState.value() ? 0L : distinctState.value());

            int counter;
            if (!mapState.contains(name)) {
                counter = 1;
                distinct += 1;
                distinctState.update(distinct);
            } else {
                counter = mapState.get(name) + 1;
            }
            mapState.put(name, counter);

            listState.add(value);

            node.put("total-" + sex, total);
            node.put("distinct-" + sex, distinct);
            node.put("counter-" + name, counter);

            return mapper.writeValueAsString(node);
        }
    }

    public static class OperatorMapFunctionWithState extends RichMapFunction<String, String> implements CheckpointedFunction {
        private static final String COUNTER_CACHE_NAME = "counter";
        private transient ListState<Long> listState;

        /* 2021年10月28日 通过使用 rich function 接口，实现本地缓存数据，这种方式跟 Operator State 的状态值保存更合理点 */
        private Long counterCache;

        private ObjectMapper mapper;

        public OperatorMapFunctionWithState(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            counterCache = 0L;
        }

        @Override
        public String map(String value) throws Exception {
            ObjectNode node = (ObjectNode) mapper.readTree(value);

            Long oldCounter = (Long) SimpleCache.getOrDefault(COUNTER_CACHE_NAME, 0L);
            Long counter = oldCounter + 1;

            // 更新缓存
            SimpleCache.set(COUNTER_CACHE_NAME, counter);

            counterCache++;

            node.put("counter", counter);
            node.put("date", FakerUtils.toLocalDateTime(node.get("date").asText()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return mapper.writeValueAsString(node);
        }

        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            // 将之前的 checkpoint 清理掉
            listState.clear();

            Long counter = (Long) SimpleCache.get(COUNTER_CACHE_NAME);
            System.err.println("快照保存的SimpleCache：" + counter);
            System.err.println("快照保存的CounterCache：" + counterCache);

            // 将最新的写入状态中
            listState.add(counter);
        }

        /**
         * 注意：运行的阶段只会执行一次从checkpoint中获取状态
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            ListStateDescriptor<Long> listStateDescriptor = new ListStateDescriptor<>("operator count", Long.class);
            listState = context.getOperatorStateStore().getListState(listStateDescriptor);

            for (Long i : listState.get()) {
                SimpleCache.set(COUNTER_CACHE_NAME, i);
                counterCache = i;
            }

            System.err.println("还原快照的SimpleCache：" + SimpleCache.get(COUNTER_CACHE_NAME));
            System.err.println("还原快照的CounterCache：" + counterCache);
        }
    }

    public static void main(String[] args) throws Exception {
        /* 2021年10月27日 note flink不会主动去读取 flink-conf.yaml 配置文件，使用方式主动去读取配置 */
        Configuration conf = GlobalConfiguration.loadConfiguration(StateMain.class.getResource("/").getPath());
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.setParallelism(1);

        // 检查点
        env.enableCheckpointing(3000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 设置状态的管理器，使用s3对象
        /* 2021年10月27日 note 使用 s3 兼容性的minio，一定要在 env.execute() 之前将配置注册到文件系统，不然不会读取 endpoint，使用默认的 awe s3 的接口地址，参考：<https://stackoverflow.com/questions/48460533/how-to-set-presto-s3-xxx-properties-when-running-flink-from-an-ide> */
        FileSystem.initialize(conf, null);
        //env.setStateBackend(new FsStateBackend("s3://flink/state"));

        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", FakerUtils.Expression.name());
        map.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        map.put("fields.sex.expression", FakerUtils.Expression.options("F", "M"));
        map.put("fields.date.expression", FakerUtils.Expression.expression("date.past", "5", "SECONDS"));
        DataStream<String> ds = env.addSource(new JavaFakerSource(map));

        // 直接像如下方式使用的话，报错：Job execution failed.
        /* Keyed state can only be used on a 'keyed stream', i.e., after a 'keyBy()' operation. */
        // ds.map(new MapFunctionWithState()).print();

        final ObjectMapper mapper = new ObjectMapper();
        ds.map(new OperatorMapFunctionWithState(mapper))
                .keyBy(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        return mapper.readTree(value).get("sex").asText();
                    }
                }).map(new KeyedMapFunctionWithState(mapper)).print();


        env.execute("state demo");
    }
    // endregion
}
