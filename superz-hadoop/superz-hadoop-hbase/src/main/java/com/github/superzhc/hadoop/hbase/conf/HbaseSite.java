package com.github.superzhc.hadoop.hbase.conf;

import org.apache.hadoop.conf.Configuration;

/**
 * hbase-site.xml 配置
 *
 * @author superz
 * @create 2021/11/16 16:55
 */
public class HbaseSite {
    private Configuration configuration;

    public HbaseSite(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 数据目录设置 : hbase.rootdir
     * <property>
     *     <name>hbase.rootdir</name>
     *     <!--
     *     支持多种文件系统
     *     1. 本地文件系统 file:///<path>
     *     2. HDFS hdfs://<namenode>:<port>/<path>
     *     3. S3 s3://<bucket-name>;s3n://<bucket-name>
     *     4. 其他文件系统
     *     -->
     *     <value>file:///<PATH>/hbase</value>
     * </property>
     */

    /**
     * ZooKeeper 相关的几个重要配置
     * <!--ZooKeeper 集群的地址，默认值为 localhost-->
     * <property>
     *     <name>hbase.zookeeper.quorum</name>
     *     <value>namenode,datanode1,datanode2</value>
     * </property>
     * <!--默认为2181-->
     * <property>
     *     <name>hbase.zookeeper.property.clientPort</name>
     *     <value>2181</value>
     * </property>
     * <!--默认为 /hbase-->
     * <property>
     *     <name>zookerper.znode.parent</name>
     *     <value>/hbase</value>
     * </property>
     */
}
