package com.github.superzhc.kafka.jsdz;

import com.github.superzhc.kafka.tool.MyProducerTool;

/**
 * @author superz
 * @create 2021/3/29 15:48
 */
public abstract class BasicEventProducer extends MyProducerTool {
    @Override
    protected String brokers() {
        return "namenode:9092,datanode1:9092,datanode2:9092";
    }
}
