package com.github.superzhc.hadoop.hbase.source;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/11/16 17:49
 */
public class HBaseConnection {
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
}
