package com.github.superzhc.hadoop.hbase.source;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author superz
 * @create 2021/11/16 17:49
 */
public class HBaseConnection implements Closeable {
    /**
     * Connection是HBase客户端进行一切操作的基础，它维持了客户端到整个HBase集群的连接，例如一个HBase集群中有2个Master、5个RegionServer，那么一般来说，这个Connection会维持一个到ActiveMaster的TCP连接和5个到RegionServer的TCP连接。
     * <p>
     * 通常，一个进程只需要为一个独立的集群建立一个Connection即可，并不需要建立连接池。建立多个连接，是为了提高客户端的吞吐量，连接池是为了减少建立和销毁连接的开销，而HBase的Connection本质上是由连接多个节点的TCP链接组成，客户端的请求分发到各个不同的物理节点，因此吞吐量并不存在问题；另外，客户端主要负责收发请求，而大部分请求的响应耗时都花在服务端，所以使用连接池也不一定能带来更高的效益。
     * <p>
     * Connection还缓存了访问的Meta信息，这样后续的大部分请求都可以通过缓存的Meta信息定位到对应的RegionServer。
     */
    private Connection connection;

    public HBaseConnection(HBaseConfig config) throws IOException {
        this.connection = ConnectionFactory.createConnection(config.configuration());
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 获取表
     * <p>
     * Table 是一个非常轻量级的对象，它实现了用于访问表的所有 API 操作，例如 Put、Get、Delete、Scan 等。
     * 本质上，它所使用的连接资源、配置信息、线程池、Meta 缓存等，都来自于 Connection 对象，因此，由同一个 Connection 创建的多个 Table，都会共享连接、配置信息、线程池、Meta 缓存等资源。
     * <p>
     * 注意事项：在请求执行完之后需要关闭 Table 对象
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    public Admin getAdmin() throws IOException {
        return connection.getAdmin();
    }

    @Override
    public void close() throws IOException {
        if (null != connection) {
            connection.close();
        }
    }
}
