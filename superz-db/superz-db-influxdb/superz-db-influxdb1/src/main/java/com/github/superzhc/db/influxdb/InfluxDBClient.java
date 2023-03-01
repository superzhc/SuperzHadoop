package com.github.superzhc.db.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/2/28 10:07
 **/
public class InfluxDBClient {
    private static final Logger LOG = LoggerFactory.getLogger(InfluxDBClient.class);

    private String url;

    private String username;

    private String password;

    private volatile InfluxDB client;

    public InfluxDBClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public InfluxDB getClient() {
        if (null == client) {
            synchronized (this) {
                if (null == client) {
                    client = InfluxDBFactory.connect(url, username, password);
                }
            }
        }
        return client;
    }

    public void createDB(String db){
        String influxQL = String.format("CREATE DATABASE %s", db);
        Query query=new Query(influxQL);
        QueryResult result= getClient().query(query);
    }
}
