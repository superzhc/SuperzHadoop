package com.github.superzhc.db.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2023/2/28 10:07
 **/
public class InfluxDBClient {
    private static final Logger LOG = LoggerFactory.getLogger(InfluxDBClient.class);

    private String url;

    private String username = null;

    private String password = null;

    private volatile InfluxDB client;

    public InfluxDBClient(String url) {
        this(url, null, null);
    }

    public InfluxDBClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public InfluxDB getClient() {
        if (null == client) {
            synchronized (this) {
                if (null == client) {
                    if (null == username || username.trim().length() == 0) {
                        client = InfluxDBFactory.connect(url);
                    } else {
                        client = InfluxDBFactory.connect(url, username, password);
                    }
                }
            }
        }
        return client;
    }

    public void createDB(String db) {
        String influxQL = String.format("CREATE DATABASE %s", db);
        Query query = new Query(influxQL);
        QueryResult result = getClient().query(query);
    }

    public List<String> measurements(String db) {
        Query query = new Query("SHOW MEASUREMENTS", db);
        QueryResult result = getClient().query(query);
        QueryResult.Series series = result.getResults().get(0).getSeries().get(0);
        List<String> lst = series.getValues().stream().map(d -> String.valueOf(d.get(0))).collect(Collectors.toList());
        return lst;
    }

    public Map<String, String> tags(String db, String measurement) {
        Query query = new Query(String.format("SHOW TAG KEYS FROM %s", measurement), db);
        QueryResult result = getClient().query(query);
        QueryResult.Series series = result.getResults().get(0).getSeries().get(0);
        List<List<Object>> values = series.getValues();

        Map<String, String> map = new LinkedHashMap<>();
        for (List<Object> value : values) {
            map.put(String.valueOf(value.get(0)), "string");
        }
        return map;
    }

    public Map<String, String> fields(String db, String measurement) {
        Query query = new Query(String.format("SHOW FIELD KEYS FROM %s", measurement), db);
        QueryResult result = getClient().query(query);
        QueryResult.Series series = result.getResults().get(0).getSeries().get(0);
        List<List<Object>> values = series.getValues();

        Map<String, String> map = new LinkedHashMap<>();
        for (List<Object> value : values) {
            map.put(String.valueOf(value.get(0)), String.valueOf(value.get(1)));
        }
        return map;
    }

    public Map<String, String> keys(String db, String measurement) {
        Map<String, String> map = new LinkedHashMap<>();
        // influxdb 默认会有一个time字段
        map.put("time", "string");
        map.putAll(tags(db, measurement));
        map.putAll(fields(db, measurement));
        return map;
    }

    public List<Map<String, Object>> query(String db, String influxQL) {
        Query query = new Query(influxQL, db);
        QueryResult result = getClient().query(query);
        QueryResult.Series series = result.getResults().get(0).getSeries().get(0);

        List<String> columns = series.getColumns();
        List<List<Object>> values = series.getValues();

        List<Map<String, Object>> data = new ArrayList<>(values.size());
        for (List<Object> value : values) {
            Map<String, Object> item = new HashMap<>();
            for (int i = 0, len = columns.size(); i < len; i++) {
                item.put(columns.get(i), value.get(i));
            }
            data.add(item);
        }
        return data;
    }
}
