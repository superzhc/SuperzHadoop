package com.github.superzhc.kafka.jsdz;

import com.github.superzhc.data.jsdz.generator.ObjectPTCEventDetailData;

/**
 * 毫米波事件的生产者
 *
 * @author superz
 * @create 2021/3/29 15:47
 */
public class ObjectPTCEventProducer extends BasicEventProducer {
    @Override
    protected String topic() {
        return "superz-test";
    }

    @Override
    protected String message() {
        return new ObjectPTCEventDetailData().convert2String();
    }

    public static void main(String[] args) {
        new ObjectPTCEventProducer().run(args);
    }
}
