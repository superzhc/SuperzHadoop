package com.github.superzhc.hadoop.hdfs.httpfs;

import com.github.superzhc.hadoop.hdfs.HdfsRestApi;

/**
 * HttpFs
 *
 * HttpFS 是一台提供 REST HTTP 网关的服务器，该网关支持所有 HDFS 文件系统操作（读取和写入）。
 *
 * HttpFS 可用于访问防火墙后面的群集上的 HDFS 中的数据（HttpFS 服务器充当网关，并且是唯一允许将防火墙穿过群集进入群集的系统）。
 *
 * 配置：在 core-site.xml 中添加如下配置：
 * <property>
 *     <name>hadoop.proxyuser.root.hosts</name>
 *     <value>*</value>
 * </property>
 * <property>
 *     <name>hadoop.proxyuser.root.groups</name>
 *     <value>*</value>
 * </property>
 * 注意：root 表示使用 root 用户运行 HttpFS 服务。
 * @author superz
 * @create 2022/3/15 15:39
 **/
public class MyHttpFS extends HdfsRestApi {

    public MyHttpFS(String host, int port) {
        super(host, port);
    }
}
