package com.github.superzhc.hadoop.clickhouse.client;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/12 17:20
 **/
public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private static final String DEFAULT_USER = "default";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_PROTOCOL = "http";
    private static final Integer DEFAULT_HTTP_PORT = 8123;
    private static final Integer DEFAULT_HTTPS_PORT = 8443;
    private static final String DEFAULT_DATABASE = "default";

    private String protocol;
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String database = null;
    // 默认格式化
    private String format = "TabSeparated";

    public HttpClient(String host) {
        this(DEFAULT_PROTOCOL, host, DEFAULT_HTTP_PORT, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public HttpClient(String protocol, String host) {
        this(protocol, host, "http".equalsIgnoreCase(protocol) ? DEFAULT_HTTP_PORT : DEFAULT_HTTPS_PORT, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public HttpClient(String host, Integer port) {
        this(DEFAULT_PROTOCOL, host, port, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public HttpClient(String protocol, String host, Integer port) {
        this(protocol, host, port, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public HttpClient(String host, Integer port, String username, String password) {
        this(DEFAULT_PROTOCOL, host, port, username, password);
    }

    public HttpClient(String protocol, String host, Integer port, String username, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    private String url() {
        return url(null);
    }

    private String url(String path) {
        /**
         * 用户名和密码可以通过以下三种方式指定：
         * 1. 通过HTTP Basic Authentication。示例：echo 'SELECT 1' | curl 'http://user:password@localhost:8123/' -d @-
         * 2. 通过URL参数中的user和password。示例：echo 'SELECT 1' | curl 'http://localhost:8123/?user=user&password=password' -d @-
         * 3. 使用X-ClickHouse-User或X-ClickHouse-Key头指定，示例:curl -H 'X-ClickHouse-User: user' -H 'X-ClickHouse-Key: password' 'http://localhost:8123/' -d @-
         */

        String username = (null == this.username || this.username.trim().length() == 0) ? DEFAULT_USER : this.username;
        String password = null == this.password ? DEFAULT_PASSWORD : this.password;
        path = null == path ? "" : (path.startsWith("/") ? path.substring(1) : path);

        return String.format("%s://%s:%s@%s:%d/%s", protocol.toLowerCase(), username, password, host, port, path);
    }

    public void useDatabase(String db) {
        this.database = db;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean health() {
        String url = url();
        int code = HttpRequest.get(url).code();
        return 200 == code;
    }

    public boolean ping() {
        String url = url("ping");
        int code = HttpRequest.get(url).code();
        return 200 == code;
    }

    public String query(String sql) {
        return query(this.database, sql);
    }

    public String query(String db, String sql) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", sql);
        params.put("default_format", format);
        // 函数传入的db优先级最高，即使为null的情况也是如实使用
        if (null != db && db.trim().length() > 0) {
            params.put("database", db);
        }

        String result = HttpRequest.get(url(), params).body();
        return result;
    }

    /**
     * 形如如下方式使用：
     * echo '(4),(5),(6)' | curl 'http://localhost:8123/?query=INSERT%20INTO%20t%20VALUES' --data-binary @-
     *
     * @param query
     * @param data
     * @return
     */
    public String queryExecute(String query, String data) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String result = HttpRequest.post(url(), params).send(data).body();
        return result;
    }

    /**
     * 创建表、插入等操作数据使用
     *
     * @param sql
     * @return
     */
    public String execute(String sql) {
        String result = HttpRequest.post(url()).send(sql).body();
        return result;
    }
}
