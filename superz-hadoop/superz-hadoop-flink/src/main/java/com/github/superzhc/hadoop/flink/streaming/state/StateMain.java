package com.github.superzhc.hadoop.flink.streaming.state;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.state.*;

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
    public static void main(String[] args) throws Exception {

    }

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

    static class OperatorState {
        /**
         * 这种状态以一个列表的形式序列化并存储，以适应横向扩展时状态重分布的问题
         * @param <T>
         * @return
         */
        public <T> ListState<T> listState(){
            return null;
        }

//        public <T> UnionListState<T> unionListState(){
//            return null;
//        }

        public <K,V> BroadcastState<K,V> broadcastState(){
            return null;
        }
    }
}
