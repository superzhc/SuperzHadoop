package com.github.superzhc.kafka.jsdz;

import com.github.superzhc.kafka.jsdz.generator.BayonetPassEventDetailData;

/**
 * 卡口事件的生产者
 * @author superz
 * @create 2021/3/26 17:52
 */
public class BayonetPassEventProducer extends BasicEventProducer {
    @Override
    protected String topic() {
        return "superz-test";
    }

    @Override
    protected String message() {
        return new BayonetPassEventDetailData().convert2String();
    }

    public static void main(String[] args) {
        new BayonetPassEventProducer().run(args);
    }
}
