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

    private static final String DEFAULT_PROTOCOL = "http";

    private String url;

//    private String host;
//
//    private Integer port;

    private boolean isDebug = false;

    private String username = null;

    private String password = null;

    public RestApi(String host, Integer port) {
        this(DEFAULT_PROTOCOL, host, port, null, null);
    }

    public RestApi(String host, Integer port, String username, String password) {
        this(DEFAULT_PROTOCOL, host, port, username, password);
    }

    public RestApi(String protocol, String host, Integer port) {
        this(protocol, host, port, null, null);
    }

    public RestApi(String protocol, String host, Integer port, String username, String password) {
        this(String.format("%s://%s:%d", protocol, host, port), username, password);
    }

    public RestApi(String url) {
        this(url, null, (String) null);
    }

    public RestApi(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void enableDebug(){
        this.isDebug=true;
    }

    public void disableDebug(){
        this.isDebug=false;
    }

    public String ping() {
        String api = String.format("%s/ping", url);

        Map<String, Object> params = new HashMap<>();
        if (username != null && null != password) {
            params.put("u", username);
            params.put("p", password);
        }

        String result = HttpRequest.get(api, params).body();
        return result;
    }

    /**
     * 创建数据库
     *
     * @param db
     */
    public String createDB(String db) {
        String influxQL = String.format("CREATE DATABASE %s", db);
        return queryPost(influxQL);
    }

    public String dropDB(String db) {
        String influxQL = String.format("DROP DATABASE %s", db);
        return query(influxQL);
    }

    public String databases() {
        String influxQL = "SHOW DATABASES";
        return query(influxQL);
    }

    /**
     * 返回指定数据库的保留策略列表
     *
     * @param db
     * @return
     */
    public String retentionPolicies(String db) {
        // 方式一
        // String influxQL=String.format("SHOW RETENTION POLICIES ON \"%s\"",db);
        // return query(influxQL);

        // 方式二
        String influxQL = "SHOW RETENTION POLICIES";
        return query(db, influxQL);
    }

    public String series(String db) {
        // 方式一
        // String influxQL = String.format("SHOW SERIES ON %s", db);
        // return query(influxQL);

        // 方式二
        String influxQL = "SHOW SERIES";
        return query(db, influxQL);
    }

    public String measurements(String db) {
        // 方式一
        // String influxQL = String.format("SHOW MEASUREMENTS ON %s", db);
        // return query(influxQL);

        // 方式二
        String influxQL = "SHOW MEASUREMENTS";
        return query(db, influxQL);
    }

    public String tagKeys(String db) {
        // 方式一
        // String influxQL = String.format("SHOW TAG KEYS ON %s", db);
        // return query(influxQL);

        // 方式二
        String influxQL = "SHOW TAG KEYS";
        return query(db, influxQL);
    }

    public String tagKeys(String db, String measurement) {
        // 方式一
        // String influxQL = String.format("SHOW TAG KEYS ON %s FROM \"%s\"", db,measurement);
        // return query(influxQL);

        // 方式二
        String influxQL = String.format("SHOW TAG KEYS FROM %s", measurement);
        return query(db, influxQL);
    }

    public String fieldKeys(String db) {
        // 方式一
        // String influxQL = String.format("SHOW FIELD KEYS ON %s", db);
        // return query(influxQL);

        // 方式二
        String influxQL = "SHOW FIELD KEYS";
        return query(db, influxQL);
    }

    public String fieldKeys(String db, String measurement) {
        // 方式一
        // String influxQL = String.format("SHOW FIELD KEYS ON %s FROM \"%s\"", db,measurement);
        // return query(influxQL);

        // 方式二
        String influxQL = String.format("SHOW FIELD KEYS FROM %s", measurement);
        return query(db, influxQL);
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

        String api = String.format("%s/write", url);

        Map<String, Object> params = new HashMap<>();
        params.put("db", db);
        if ((null != username && username.trim().length() > 0) && (null != password && password.trim().length() > 0)) {
            params.put("u", username);
            params.put("p", password);
        }

        HttpRequest request = HttpRequest.post(api, params).send(influxQL);
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
        return read(db, null, influxQL, null);
    }

    /**
     * `SELECT`支持指定数据的几种格式：
     * 1. `SELECT *`：返回所有的field和tag
     * 2. `SELECT f1` 返回特定的field
     * 3. `SELECT f1,f2` 返回多个field
     * 4. `SELECT t1,f1` 返回特定的field和tag，`SELECT`在包括一个tag时，必须至少指定一个field
     * 5. `SELECT a1::field,a1::tag` 返回特定的field和tag，`::[field | tag]`语法指定标识符的类型。 使用此语法来区分具有相同名称的field key和tag key。
     *
     * @param db
     * @param rp
     * @param influxQL
     * @param epoch    在InfluxDB中的所有数据都是存的UTC时间，时间戳默认返回RFC3339格式的纳米级的UTC时间，例如2015-08-04T19:05:14.318570484Z，如果想要返回Unix格式的时间，可以在请求参数里设置epoch参数，其中epoch可以是[h,m,s,ms,u,ns]之一。
     * @return
     */
    public String read(String db, String rp, String influxQL, String epoch) {
        LOG.info(influxQL);

        Map<String, Object> params = new HashMap<>();
        // params.put("db", db);
        if (null != rp && rp.trim().length() > 0) {
            params.put("rp", rp);
        }
        // params.put("q", influxQL);
        if (null != epoch && epoch.trim().length() > 0) {
            params.put("epoch", epoch);
        }

        return query(db, influxQL, params);
    }

    public String query(String influxQL) {
        return query(null, influxQL, null);
    }

    public String query(String db, String influxQL) {
        return query(db, influxQL, null);
    }

    public String query(String db, String influxQL, Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>();
        }

        if (null != db && db.trim().length() > 0) {
            params.put("db", db);
        }

        params.put("q", influxQL);
        return query(params);
    }

    public String query(Map<String, Object> params) {
        String api = String.format("%s/query", url);

        params.put("pretty", isDebug);
        if ((null != username && username.trim().length() > 0) && (null != password && password.trim().length() > 0)) {
            params.put("u", username);
            params.put("p", password);
        }

        String result = HttpRequest.get(api, params).body();
        return result;
    }

    public String queryPost(String influxQL) {
        return queryPost(null, influxQL, null);
    }

    public String queryPost(String db, String influxQL) {
        return queryPost(db, influxQL, null);
    }

    public String queryPost(String db, String influxQL, Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>();
        }

        if (null != db && db.trim().length() > 0) {
            params.put("db", db);
        }

        params.put("q", influxQL);
        return queryPost(params);
    }

    public String queryPost(Map<String, Object> params) {
        String api = String.format("%s/query", url);

        params.put("pretty", isDebug);
        if ((null != username && username.trim().length() > 0) && (null != password && password.trim().length() > 0)) {
            params.put("u", username);
            params.put("p", password);
        }

        String result = HttpRequest.post(api, params).body();
        return result;
    }

//    public boolean isDebug() {
//        return isDebug;
//    }
//
//    public void setDebug(boolean debug) {
//        isDebug = debug;
//    }
}
