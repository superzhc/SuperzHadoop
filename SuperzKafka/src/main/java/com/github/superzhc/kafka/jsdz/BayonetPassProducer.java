package com.github.superzhc.kafka.jsdz;

import com.github.superzhc.data.jsdz.generator.BayonetPassEventDetailData;
import com.github.superzhc.kafka.tool.MyProducerTool;

/**
 * @author superz
 * @create 2021/3/26 17:52
 */
public class BayonetPassProducer extends MyProducerTool {
    @Override
    protected String topic() {
        return "superz-test";
    }

    @Override
    protected String brokers() {
        return "namenode:9092,datanode1:9092,datanode2:9092";
    }

    @Override
    protected String message() {
        return new BayonetPassEventDetailData().convert2String();
    }

    public static void main(String[] args) {
        new BayonetPassProducer().run(args);
    }
}
