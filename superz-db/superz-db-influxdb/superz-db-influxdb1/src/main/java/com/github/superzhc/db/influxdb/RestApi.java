package com.github.superzhc.db.influxdb;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2023/2/22 10:11
 **/
public class RestApi {
    private static final Logger LOG = LoggerFactory.getLogger(RestApi.class);

    private String host;

    private Integer port;

    private boolean isDebug = false;

    public RestApi(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public RestApi(String host, Integer port, boolean isDebug) {
        this.host = host;
        this.port = port;
        this.isDebug = isDebug;
    }

    /**
     * 创建数据库
     *
     * @param db
     */
    public String createDB(String db) {
        String influxQL = String.format("CREATE DATABASE %s", db);

        String url = String.format("http://%s:%d/query", host, port);

        Map<String, Object> params = new HashMap<>();
        params.put("q", influxQL);

        String result = HttpRequest.post(url).form(params).body();
        return result;
    }

    /**
     * 设置保留策略
     * <p>
     * 在创建数据库时未设置RP，InfluxDB会自动生成一个叫做autogen的RP，并作为数据库的默认RP，autogen这个RP会永远保留数据。
     *
     * @param db
     * @param rpName
     * @param duration
     * @param replication 复制片参数(REPLICATION 1)是必须的，但是对于单个节点的InfluxDB实例，复制片只能设为1
     * @param isDefault   是否是数据库{@param db}的默认策略
     */
    public void createRP(String db, String rpName, String duration, Integer replication, boolean isDefault) {
        String influxQL = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %d%s",
                rpName
                , db
                , duration
                , replication
                , isDefault ? " DEFAULT" : ""
        );
    }

    public String writeBatch(String db, List<LineProtocol> lineProtocols) {
        String influxQL = LineProtocol.buildBatch(lineProtocols);
        LOG.info("[Line Protocol]: {}", influxQL);

        return write(db, influxQL);
    }

    public String write(String db, LineProtocol lineProtocol) {
        return write(db, lineProtocol.getMeasurement(), lineProtocol.getTagSet(), lineProtocol.getFieldSet(), lineProtocol.getTimestamp());
    }

    public String write(String db, String measurement, Map<String, Object> fields) {
        return write(db, measurement, null, fields, null);
    }

    public String write(String db, String measurement, Map<String, Object> fields, Long timestamp) {
        return write(db, measurement, null, fields, timestamp);
    }

    public String write(String db, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        return write(db, measurement, tags, fields, null);
    }

    public String write(String db, String measurement, Map<String, String> tags, Map<String, Object> fields, Long timestamp) {
        String influxQl = LineProtocol.build(measurement, tags, fields, timestamp);
        return write(db, influxQl);
    }

    public String write(String db, String influxQL) {
        LOG.info("[Line Protocol]: {}", influxQL);

        String url = String.format("http://%s:%d/write?db=%s", host, port, db);

        HttpRequest request = HttpRequest.post(url).send(influxQL);
        if (request.code() == 204) {
            return "{}";
        } else {
            return request.body();
        }
    }

    public String read(String db, List<String> influxQL) {
        /**
         * 多个查询
         * 在一次API调用中发送多个InfluxDB的查询语句，可以简单地使用分号分隔每个查询
         */
        String finalInfluxQL = influxQL.stream().collect(Collectors.joining(";"));
        return read(db, finalInfluxQL);
    }

    public String read(String db, String influxQL) {
        return read(db, influxQL, null);
    }

    /**
     * @param db
     * @param influxQL
     * @param epoch    在InfluxDB中的所有数据都是存的UTC时间，时间戳默认返回RFC3339格式的纳米级的UTC时间，例如2015-08-04T19:05:14.318570484Z，如果想要返回Unix格式的时间，可以在请求参数里设置epoch参数，其中epoch可以是[h,m,s,ms,u,ns]之一。
     * @return
     */
    public String read(String db, String influxQL, String epoch) {
        String url = String.format("http://%s:%d/query", host, port);

        Map<String, Object> params = new HashMap<>();
        params.put("db", db);
        params.put("q", influxQL);
        params.put("pretty", isDebug);
        if (null != epoch && epoch.trim().length() > 0) {
            params.put("epoch", epoch);
        }

        String result = HttpRequest.get(url, params).body();
        return result;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
