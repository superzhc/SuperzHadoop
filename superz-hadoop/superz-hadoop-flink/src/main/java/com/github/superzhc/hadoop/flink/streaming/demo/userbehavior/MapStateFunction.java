package com.github.superzhc.hadoop.flink.streaming.demo.userbehavior;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * @author superz
 * @create 2021/10/12 15:13
 */
public class MapStateFunction extends RichFlatMapFunction<UserBehavior, Tuple3<Long, String, Integer>> {
    /* 定义 MapState */
    private MapState<String, Integer> behaviorMapState;

    @Override
    public void open(Configuration parameters) throws Exception {
        // 创建 StateDescriptor
        MapStateDescriptor<String, Integer> behaviorMapStateDescriptor = new MapStateDescriptor<String, Integer>("behaviorMap", String.class, Integer.class);
        // 通过 StateDescriptor 获取运行时上下文中的状态
        behaviorMapState = getRuntimeContext().getMapState(behaviorMapStateDescriptor);
    }

    @Override
    public void flatMap(UserBehavior value, Collector<Tuple3<Long, String, Integer>> out) throws Exception {
        Integer behaviorCnt = 1;
        // behavior 有可能为 点击(pv)、购买(buy)、加购物车(cart)、喜欢(fav) 等
        // 判断状态中是否有该 behavior
        if (behaviorMapState.contains(value.getBehavior())) {
            behaviorCnt = behaviorMapState.get(value.getBehavior()) + 1;
        }
        // 更新状态
        behaviorMapState.put(value.getBehavior(), behaviorCnt);
        out.collect(new Tuple3<>(value.getUserId(), value.getBehavior(), behaviorCnt));
    }
}
