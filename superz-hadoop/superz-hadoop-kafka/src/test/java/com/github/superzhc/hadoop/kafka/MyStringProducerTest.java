package com.github.superzhc.hadoop.kafka;

import org.junit.After;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/30 11:48
 **/
public class MyStringProducerTest {
    MyStringProducer producer=null;

    @Before
    public void setUp(){
        producer=new MyStringProducer("127.0.0.1:19092");
    }

    @After
    public void tearDown(){}
}
