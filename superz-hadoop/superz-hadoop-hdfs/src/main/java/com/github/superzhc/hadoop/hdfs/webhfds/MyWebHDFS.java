package com.github.superzhc.hadoop.hdfs.webhfds;

import com.github.superzhc.hadoop.hdfs.RestApiHDFS;

/**
 * WebHDFS
 * <p>
 * 配置：在 hdfs-site.xml 控制 WebHDFS 开关的属性，默认是打开
 * <property>
 * <name>dfs.webhdfs.enabled</name>
 * <value>true</value>
 * </property>
 * <p>
 * 当 client 请求某个文件时，WebHDFS 会将其重定向到该资源所在的 DataNode 节点上
 *
 * @author superz
 * @create 2022/3/14 17:48
 **/
public class MyWebHDFS extends RestApiHDFS {

    public MyWebHDFS(String host, int port) {
        super(host, port);
    }
}
