package com.github.superzhc.hadoop.storm0.spout;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.shopping.SMZDM;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/4 10:57
 **/
public class SMZDMSpout extends BaseRichSpout {
    private SpoutOutputCollector collector = null;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        long start = System.currentTimeMillis();

        List<Map<String, Object>> data = SMZDM.ranking(SMZDM.RankType.好价电商榜_京东);
        for (Map<String, Object> item : data) {
            collector.emit(new Values(JsonUtils.asString(item)));
        }

        long end = System.currentTimeMillis();
        try {
            // 每五分钟执行一次
            Thread.sleep(1000 * 60 * 5 - (end - start));
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("data"));
    }
}
