package com.github.superzhc.hadoop.kafka.demo;

import com.github.superzhc.hadoop.kafka.MyAdminClient;

/**
 * @author superz
 * @create 2022/11/15 14:59
 **/
public class AdminClientMain {
    public static void main(String[] args) {
        String brokers = "localhost:19092";
        String topic="";

        try (MyAdminClient client = new MyAdminClient(brokers)) {
            client.create("smzdm_ranking", 20, (short) 1, null);
        }
    }
}
