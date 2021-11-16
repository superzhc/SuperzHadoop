package com.github.superzhc.hadoop.hbase.source.impl;

import com.github.superzhc.hadoop.hbase.source.HBaseConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * @author superz
 * @create 2021/11/16 17:42
 */
public class Cloud4ControlConfig implements HBaseConfig {
    @Override
    public Configuration configuration() {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 如果是集群 则主机名用逗号分隔
        configuration.set("hbase.zookeeper.quorum", "namenode,datanode1,datanode2");
        return configuration;
    }
}
