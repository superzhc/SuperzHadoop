package com.github.superzhc.hadoop.storm0.bolt;

import com.github.superzhc.common.jackson.JsonUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @author superz
 * @create 2022/11/4 14:39
 **/
public class DealBolt extends BaseRichBolt {
    private OutputCollector collector = null;

    private final String[] keys;

    public DealBolt(String... keys) {
        this.keys = keys;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String data = tuple.getStringByField("data");
        Map<String, Object> map = JsonUtils.map(data);

        Object[] objs = new Object[keys.length];
        for (int i = 0, len = keys.length; i < len; i++) {
            objs[i] = map.get(keys[i]);
        }

        collector.emit(tuple, new Values(objs));
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(keys));
    }
}
