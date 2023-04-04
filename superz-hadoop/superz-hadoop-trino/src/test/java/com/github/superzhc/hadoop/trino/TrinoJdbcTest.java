package com.github.superzhc.hadoop.trino;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrinoJdbcTest {

    /**
     * Trino 支持以下 JDBC 的 URL 格式：
     * jdbc:trino://host:port
     * jdbc:trino://host:port/catalog
     * jdbc:trino://host:port/catalog/schema
     */
    String url = "jdbc:trino://10.90.15.221:7099";
    String username = "root";
    String password = null;

    JdbcHelper jdbc = null;

    @Before
    public void setUp() throws Exception {
        jdbc = new JdbcHelper(url, username, password);
    }

    @After
    public void tearDown() throws Exception {
        if (null != jdbc) {
            jdbc.close();
        }
    }

    @Test
    public void catalogs() {
        String sql = "SHOW CATALOGS";
        jdbc.show(sql);
    }

    @Test
    public void schemas() {
        // SHOW SCHEMAS IN <catalog>
        String sql = "SHOW SCHEMAS IN elasticsearch";
        jdbc.show(sql);
    }

    @Test
    public void tables() {
        // SHOW TABLES IN <catalog>.<schema>
        String sql = "SHOW TABLES IN iceberg.xgitbigdata";
        jdbc.show(sql);
    }

    @Test
    public void esIndices() {
        String sql = "SHOW TABLES IN elasticsearch.xgitbigdata";
        jdbc.show(sql);
    }

    @Test
    public void describe() {
        jdbc.ddlExecute("USE iceberg.xgitbigdata");

        String sql = "DESCRIBE t202303091709";
        jdbc.show(sql);
    }
}