package com.github.superzhc.common.jdbc;


import com.github.superzhc.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;
import java.util.*;

/**
 * 2020年11月04日 superz add
 */
public class JdbcHelper implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(JdbcHelper.class);

    private static final Integer DEFAULT_SHOW_NUMBER = 20;

    public static class Driver {
        public static final String MySQL = "com.mysql.jdbc.Driver";
        public static final String MySQL8 = "com.mysql.cj.jdbc.Driver";
        public static final String Oracle = "oracle.jdbc.driver.OracleDriver";
        public static final String SQLServer = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        public static final String SQLServer_v2 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        public static final String PostgreSQL = "org.postgresql.Driver";
        public static final String DB2 = "com.ibm.db2.jdbc.app.DB2.Driver";
        public static final String Informix = "com.informix.jdbc.IfxDriver";
        public static final String Sysbase = "com.sybase.jdbc.SybDriver";
        public static final String ODBC = "sun.jdbc.odbc.JdbcOdbcDriver";

        public static String match(String url) {
            if (url.startsWith("jdbc:mysql:")) {
                return MySQL;
            } else if (url.startsWith("jdbc:microsoft:sqlserver:")) {
                return SQLServer;
            } else if (url.startsWith("jdbc:oracle:thin:")) {
                return Oracle;
            } else if (url.startsWith("jdbc:postgresql:")) {
                return PostgreSQL;
            } else if (url.startsWith("jdbc:db2:")) {
                return DB2;
            } else if (url.startsWith("jdbc:Informix-sqli:")) {
                return Informix;
            } else if (url.startsWith("jdbc:Sysbase:")) {
                return Sysbase;
            } else if (url.startsWith("jdbc:odbc:")) {
                return ODBC;
            }
            return null;
        }
    }

    public static class Page {
        private JdbcHelper jdbc;
        private String table;
        private Integer start = 0;
        private Integer size;

        public Page(JdbcHelper jdbc, String table, Integer size) {
            this(jdbc, table, 0, size);
        }

        public Page(JdbcHelper jdbc, String table, Integer start, Integer size) {
            this.jdbc = jdbc;
            this.table = table;
            this.start = start;
            this.size = size;
        }

        public String sql() {
            String url = jdbc.dbConfig.getUrl();
            if (url.startsWith("jdbc:mysql:")) {
                return mysql();
            } else if (url.startsWith("jdbc:microsoft:sqlserver:")) {
                return sqlServer2005();
            } else if (url.startsWith("jdbc:oracle:thin:")) {
                return oracle();
            } else if (url.startsWith("jdbc:postgresql:")) {
                return postgreSql();
            } else if (url.startsWith("jdbc:db2:")) {
                return db2();
            } else if (url.startsWith("jdbc:odbc:")) {
                return null;
            }
            return null;
        }

        public String oracle() {
            return String.format("SELECT * FROM (SELECT a.*,ROWNUM FROM %s AS a WHERE ROWNUM<=%d) WHERE ROWNUM>%d", table, start + size, start);
        }

        public String db2() {
            ResultSet rs = null;
            try {
                // 随机获取一列的列名
                Connection connection = jdbc.getConnection();
                DatabaseMetaData meta = connection.getMetaData();
                rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), table, "%");
                if (rs.next()) {
                    String column = rs.getString("TABLE_NAME");
                    String sqlTemplate = "SELECT * FROM (SELECT ROWNUMBER() OVER() AS rc,a.* FROM(SELECT * FROM %s ORDER BY %s) as a) WHERE rc BETWEEN %d AND %d";
                    return String.format(sqlTemplate, table, column, start, start + size);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jdbc.free(null, null, rs);
            }
            return null;
        }

        public String sqlServer2000() {
            ResultSet rs = null;
            try {
                // 随机获取一列的列名
                Connection connection = jdbc.getConnection();
                DatabaseMetaData meta = connection.getMetaData();
                rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), table, "%");
                if (rs.next()) {
                    String column = rs.getString("TABLE_NAME");
                    String sqlTemplate = "SELECT TOP %d * FROM %s WHERE %s NOT IN(SELECT TOP %d %s FROM %s ORDER BY %s) ORDER BY %s";
                    return String.format(sqlTemplate, size, table, column, start + size, column, table, column, column);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jdbc.free(null, null, rs);
            }
            return null;
        }

        public String sqlServer2005() {
            ResultSet rs = null;
            try {
                // 随机获取一列的列名
                Connection connection = jdbc.getConnection();
                DatabaseMetaData meta = connection.getMetaData();
                rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), table, "%");
                if (rs.next()) {
                    String column = rs.getString("TABLE_NAME");
                    String sqlTemplate = "SELECT * FROM (SELECT ROWNUMBER() OVER(ORDER BY %s) AS rc,a.* FROM %s as a) WHERE rc BETWEEN %d AND %d";
                    return String.format(sqlTemplate, table, column, start, start + size);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jdbc.free(null, null, rs);
            }
            return null;
        }

        public String mysql() {
            return String.format("SELECT * FROM %s LIMIT %d,%d", table, start, size);
        }

        public String postgreSql() {
            return String.format("SELECT * FROM %s LIMIT %d,%d", table, size, start);
        }
    }

    public static class DBConfig {
        private String driver;
        private String url;
        private String username;
        private String password;

        public DBConfig(String driver, String url, String username, String password) {
            this.driver = driver;
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /* 定义数据库的配置信息 */
    private DBConfig dbConfig;
    /* 定义数据库的连接 */
    private Connection conn = null;

    public JdbcHelper(String url) {
        this(url, null, null);
    }

    public JdbcHelper(String url, String username, String password) {
        this(null == Driver.match(url) ? null : Driver.match(url), url, username, password);
    }

    public JdbcHelper(String driver, String url, String username, String password) {
        dbConfig = new DBConfig(driver, url, username, password);
    }

    /**
     * 开放出去，并进一步优化被关掉了也可以重新进行连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            if (null == conn || conn.isClosed()) {
                synchronized (this) {
                    if (null == conn || conn.isClosed()) {
                        if (null != dbConfig.driver) {
                            Class.forName(dbConfig.driver);
                        }
                        Properties info = new Properties();

                        if (null != dbConfig.username && dbConfig.username.trim().length() > 0) {
                            info.put("user", dbConfig.username);
                        }
                        if (null != dbConfig.password && dbConfig.password.trim().length() > 0) {
                            info.put("password", dbConfig.password);
                        }

                        conn = DriverManager.getConnection(dbConfig.url, info);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn
     */
    private void freeConnection(Connection conn) {
        try {
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 释放statement
     *
     * @param statement
     */
    private void freeStatement(Statement statement) {
        try {
            if (null != statement) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 释放resultset
     *
     * @param rs
     */
    private void freeResultSet(ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param statement
     * @param rs
     */
    public void free(Connection conn, Statement statement, ResultSet rs) {
        //if (rs != null) {
        freeResultSet(rs);
        //}
        //if (statement != null) {
        freeStatement(statement);
        //}
        //if (conn != null) {
        freeConnection(conn);
        //}
    }

    @Override
    public void close() {
        free(conn, null, null);
    }

    /**
     * 判断表是否存在
     *
     * @param table
     * @return
     */
    public boolean exist(String table) {
        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            rs = metaData.getTables(conn.getCatalog(), conn.getSchema(), table, new String[]{"TABLE"});
            return rs.next();
        } catch (Exception e) {
            log.error("判断表[" + table + "]是否存在异常，", e);
            return false;
        } finally {
            free(null, null, rs);
        }
    }

    public String[] tables() {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            List<String> tables = new ArrayList<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(connection.getCatalog(), connection.getSchema(), "%", new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

            String[] rt = new String[tables.size()];
            tables.toArray(rt);
            return rt;
        } catch (SQLException throwables) {
            log.error("获取表元数据异常", throwables);
            return null;
        } finally {
            free(null, null, rs);
        }
    }

    public String[] views() {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            List<String> result = new ArrayList<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(connection.getCatalog(), connection.getSchema(), "%", new String[]{"VIEW"});
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }

            String[] rt = new String[result.size()];
            result.toArray(rt);
            return rt;
        } catch (SQLException throwables) {
            log.error("获取表元数据异常", throwables);
            return null;
        } finally {
            free(null, null, rs);
        }
    }

    public String[] columns(String table) {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            List<String> result = new ArrayList<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), table, "%");
            while (rs.next()) {
                // 不返回自增列
                if ("YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"))) {
                    continue;
                }
                result.add(rs.getString("COLUMN_NAME"));
            }

            String[] rt = new String[result.size()];
            result.toArray(rt);
            return rt;
        } catch (SQLException throwables) {
            log.error("获取表元数据异常", throwables);
            return null;
        } finally {
            free(null, null, rs);
        }
    }

    public Map<String, String> columnAndTypes(String table) {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            // 注意用有序的map，不然获取的列是无序的，不是很好用
            Map<String, String> result = new LinkedHashMap<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), table, "%");
            while (rs.next()) {
                // 不返回自增列
                if ("YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"))) {
                    continue;
                }

                String type;
                if ("VARCHAR".equalsIgnoreCase(rs.getString("TYPE_NAME"))) {
                    // 此处做个限定
                    Integer columnSize = rs.getInt("COLUMN_SIZE");
                    if (columnSize > 1024) {
                        columnSize = 1024;
                    }
                    type = rs.getString("TYPE_NAME") + "(" + columnSize + ")";
                } else {
                    type = rs.getString("TYPE_NAME");
                }
                result.put(rs.getString("COLUMN_NAME"), type);
            }

            return result;
        } catch (SQLException throwables) {
            log.error("获取表元数据异常", throwables);
            return null;
        } finally {
            free(null, null, rs);
        }
    }

    public int ddlExecute(String sql) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        Statement stmt = null;
        try {
            log.debug("DDL 语句：{}", sql);
            stmt = getConnection().createStatement();
            return stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("DDL异常", e);
            return -1;
        } finally {
            free(null, stmt, null);
        }
    }

    public int insert(String table, String[] columns, List<Object> params) {
        return insert(table, columns, params.toArray());
    }

    public int insert(String table, String[] columns, Object... params) {
        StringBuilder columnsSb = new StringBuilder();
        StringBuilder placeholdSb = new StringBuilder();
        for (String column : columns) {
            columnsSb.append(",").append(column);
            placeholdSb.append(",?");
        }

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", table, columnsSb.substring(1), placeholdSb.substring(1));
        return dmlExecute(sql, params);
    }

    public int update(String table, Map<String, Object> params, Map<String, Object> conditions) {
        if (null == conditions || conditions.size() == 0) {
            log.debug("更新表[" + table + "]数据，条件不允许为空");
            return -1;
        }

        StringBuilder columnsSb = new StringBuilder();
        Object[] values = new Object[params.size() + conditions.size()];
        int i = 0;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            columnsSb.append(",").append(param.getKey()).append("=").append("?");
            values[i] = param.getValue();
            i++;
        }

        StringBuilder conditionColumnsSb = new StringBuilder();
        int j = 0;
        for (Map.Entry<String, Object> condition : conditions.entrySet()) {
            conditionColumnsSb.append(" AND ").append(condition.getKey()).append("=").append("?");
            values[i + j] = condition.getValue();
            j++;
        }

        String sql = String.format("UPDATE %s SET %s WHERE 1=1 %s", table, columnsSb.substring(1), conditionColumnsSb);
        return dmlExecute(sql, values);
    }

    public int update(String table, String condition, Map<String, Object> params) {
        if (null == condition || condition.trim().length() == 0) {
            log.debug("更新表[" + table + "]数据，条件不允许为空");
            return -1;
        }

        if (!condition.trim().toLowerCase().startsWith("and")) {
            condition = "AND " + condition;
        }

        StringBuilder columnsSb = new StringBuilder();
        Object[] values = new Object[params.size()];
        int i = 0;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            columnsSb.append(",").append(param.getKey()).append("=").append("?");
            values[i] = param.getValue();
        }

        String sql = String.format("UPDATE %s SET %s WHERE 1=1 %s", table, columnsSb.substring(1), condition);
        return dmlExecute(sql, values);
    }

    public int delete(String table, Map<String, Object> conditions) {
        if (null == conditions || conditions.size() == 0) {
            log.debug("删除表[" + table + "]数据，条件不允许为空");
            return -1;
        }

        StringBuilder columnsSb = new StringBuilder();
        Object[] values = new Object[conditions.size()];
        int i = 0;
        for (Map.Entry<String, Object> condition : conditions.entrySet()) {
            columnsSb.append(" AND ").append(condition.getKey()).append("=").append("?");
            values[i] = condition.getValue();
            i++;
        }

        String sql = String.format("DELETE FROM %s WHERE 1=1 %s", table, columnsSb);
        return dmlExecute(sql, values);
    }

    public int delete(String table, String condition) {
        if (null == condition || condition.trim().length() == 0) {
            log.debug("删除表[" + table + "]数据，条件不允许为空");
            return -1;
        }

        if (!condition.trim().toLowerCase().startsWith("and")) {
            condition = "AND " + condition;
        }

        String sql = String.format("DELETE FROM %s WHERE 1=1 %s", table, condition);
        return dmlExecute(sql);
    }

    /**
     * 新增/更新/删除数据
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int dmlExecute(String sql, Object... params) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        PreparedStatement pstmt = null;
        try {
            log.debug("DML 语句：{}", sql);
            pstmt = getConnection().prepareStatement(sql);
            // 填充sql语句中的占位符
            if (null != params && params.length != 0) {
                log.debug("DML 参数个数：{}", params.length);
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            // 影响行数
            int result = pstmt.executeUpdate();
            return result;
        } catch (SQLException ex) {
            log.error("DML异常", ex);
            return -1;
        } finally {
            free(null, pstmt, null);
        }
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> query(String sql, Object... params) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            log.debug("查询语句：{}", sql);
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
                log.debug("查询参数：{}", params);
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            return ResultSetUtils.Result2ListMap(rs);
        } catch (SQLException ex) {
            log.error("查询异常", ex);
            return null;
        } finally {
            free(null, pstmt, rs);
        }
    }

    public <T> List<T> queryBeans(String sql, Class<T> beanClass, Object... params) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            log.debug("查询语句：{}", sql);
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
                log.debug("查询参数：{}", params);
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            return ResultSetUtils.Result2ListBean(rs, beanClass);
        } catch (SQLException ex) {
            log.error("查询异常", ex);
            return null;
        } finally {
            free(null, pstmt, rs);
        }
    }

    public void preview(String table) {
        preview(table, DEFAULT_SHOW_NUMBER);
    }

    public void preview(String table, Integer number) {
        Page page = new Page(this, table, number);
        show(page.sql(), number);
    }

    public void show(String sql, Object... params) {
        show(sql, DEFAULT_SHOW_NUMBER, params);
    }

    public void show(String sql, Integer number, Object... params) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            log.debug("查询语句：{}", sql);
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
                log.debug("查询参数：{}", params);
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            ResultSetUtils.print(rs, number);
        } catch (SQLException ex) {
            log.error("查询异常", ex);
        } finally {
            free(null, pstmt, rs);
        }
    }

    @Deprecated
    public <T> T queryOne(String sql, Object... params) {
        return aggregate(sql, params);
    }

    /**
     * 总计
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T aggregate(String sql, Object... params) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Object result = null;
        try {
            log.debug("查询语句：{}", sql);
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
                log.debug("查询参数：{}", params);
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
                break;// 获取到第一行数据就不再获取其他行
            }
        } catch (SQLException ex) {
            log.error("查询异常", ex);
        } finally {
            free(null, pstmt, rs);
        }
        return (T) result;
    }

    /**
     * 调用存储过程执行查询
     *
     * @param sql
     * @param paramters
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> callableQuery(String sql, Object... paramters) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = getConnection().prepareCall(sql);

            if (null != paramters && paramters.length > 0) {
                for (int i = 0; i < paramters.length; i++) {
                    cstmt.setObject(i + 1, paramters[i]);
                }
            }
            rs = cstmt.executeQuery();
            return ResultSetUtils.Result2ListMap(rs);
        } catch (SQLException ex) {
            log.error("查询异常", ex);
            return null;
        } finally {
            free(null, cstmt, rs);
        }
    }

    public void callableShow(String sql, Object... paramters) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = getConnection().prepareCall(sql);

            if (null != paramters && paramters.length > 0) {
                for (int i = 0; i < paramters.length; i++) {
                    cstmt.setObject(i + 1, paramters[i]);
                }
            }
            rs = cstmt.executeQuery();
            ResultSetUtils.print(rs);
        } catch (SQLException ex) {
            log.error("查询异常", ex);
        } finally {
            free(null, cstmt, rs);
        }
    }

    public <T> T callableQueryOne(String sql, Object... paramters) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        CallableStatement cstmt = null;
        ResultSet rs = null;
        Object result = null;
        try {
            cstmt = getConnection().prepareCall(sql);

            if (null != paramters && paramters.length > 0) {
                for (int i = 0; i < paramters.length; i++) {
                    cstmt.setObject(i + 1, paramters[i]);
                }
            }
            rs = cstmt.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
                break;
            }
            return (T) result;
        } catch (SQLException ex) {
            log.error("查询异常", ex);
            return null;
        } finally {
            free(null, cstmt, rs);
        }
    }

    /**
     * 调用存储过程，执行增删改
     *
     * @param sql        存储过程
     * @param parameters
     * @return 影响行数
     * @throws SQLException
     */
    public int callableUpdate(String sql, Object... parameters) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall(sql);
            if (null != parameters && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    cstmt.setObject(i + 1, parameters[i]);
                }
            }
            return cstmt.executeUpdate();
        } catch (SQLException ex) {
            log.error("查询异常", ex);
            return -1;
        } finally {
            free(null, cstmt, null);
        }
    }

    /**
     * 批量更新数据
     *
     * @param sqlList 一组sql
     * @return
     */
    public void batchUpdate(List<String> sqlList) {
        batchUpdate(sqlList, sqlList.size());
    }

    public void batchUpdate(List<String> sqlList, Integer batchSize) {
        Statement statement = null;
        try {
            getConnection().setAutoCommit(false);
            statement = getConnection().createStatement();

            int currentBatchSize = 0;
            for (String sql : sqlList) {
                statement.addBatch(sql);

                currentBatchSize++;

                if (currentBatchSize == batchSize) {
                    statement.executeBatch();
                    getConnection().commit();
                    currentBatchSize = 0;
                    log.debug("插入" + batchSize + "条数据");
                }
            }

            if (currentBatchSize > 0) {
                statement.executeBatch();
                getConnection().commit();
            }
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        } finally {
            free(null, statement, null);
        }
    }

    public void batchUpdate(String table, String columns, List<List<Object>> params) {
        batchUpdate(table, columns, params, params.size());
    }

    public void batchUpdate(String table, String columns, List<List<Object>> params, Integer batchSize) {
        Object[][] arrParams = new Object[params.size()][];
        for (int i = 0, len = params.size(); i < len; i++) {
            List<Object> param = params.get(i);
            Object[] arrParam = new Object[param.size()];
            param.toArray(arrParam);
            arrParams[i] = arrParam;
        }
        batchUpdate(table, columns, arrParams, batchSize);
    }

    public void batchUpdate(String table, String[] columns, List<List<Object>> params) {
        batchUpdate(table, columns, params, params.size());
    }

    public void batchUpdate(String table, String[] columns, List<List<Object>> params, Integer batchSize) {
        Object[][] arrParams = new Object[params.size()][];
        for (int i = 0, len = params.size(); i < len; i++) {
            List<Object> param = params.get(i);
            Object[] arrParam = new Object[param.size()];
            param.toArray(arrParam);
            arrParams[i] = arrParam;
        }
        batchUpdate(table, columns, arrParams, batchSize);
    }

    public void batchUpdate(String sql, List<List<Object>> params) {
        batchUpdate(sql, params, params.size());
    }

    public void batchUpdate(String sql, List<List<Object>> params, Integer batchSize) {
        Object[][] arrParams = new Object[params.size()][];
        for (int i = 0, len = params.size(); i < len; i++) {
            List<Object> param = params.get(i);
            Object[] arrParam = new Object[param.size()];
            param.toArray(arrParam);
            arrParams[i] = arrParam;
        }
        batchUpdate(sql, arrParams, batchSize);
    }

    public void batchUpdate(String table, String columns, Object[][] params) {
        batchUpdate(table, columns, params, params.length);
    }

    /**
     * 批量更新
     *
     * @param table
     * @param columns   使用英文逗号（,）进行分割
     * @param params
     * @param batchSize
     */
    public void batchUpdate(String table, String columns, Object[][] params, Integer batchSize) {
        String[] arrColumns = columns.split(",");
        batchUpdate(table, arrColumns, params, batchSize);
    }

    public void batchUpdate(String table, String[] columns, Object[][] params) {
        batchUpdate(table, columns, params, params.length);
    }

    public void batchUpdate(String table, String[] columns, Object[][] params, Integer batchSize) {
        StringBuilder columnsSb = new StringBuilder();
        StringBuilder placeholdSb = new StringBuilder();
        for (String column : columns) {
            if (StringUtils.isBlank(column)) {
                continue;
            }

            columnsSb.append(",").append(column);
            placeholdSb.append(",?");
        }

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", table, columnsSb.substring(1), placeholdSb.substring(1));
        batchUpdate(sql, params, batchSize);
    }

    public void batchUpdate(String sql, Object[][] params) {
        batchUpdate(sql, params, params.length);
    }

    /**
     * 批量进行更新
     *
     * @param sql       PreparedStatement 的 SQL 语句
     * @param params    数据
     * @param batchSize 单次批处理的数据量
     */
    public void batchUpdate(String sql, Object[][] params, Integer batchSize) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }

        log.debug("batch sql:" + sql);
        log.debug("batch size:" + batchSize);
        if (null == params || params.length == 0) {
            log.debug("no data");
            return;
        }

        PreparedStatement preparedStatement = null;
        try {
            getConnection().setAutoCommit(false);
            preparedStatement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            long start = System.currentTimeMillis();
            long totalStart = start;
            int currentBatchSize = 0;
            int totalSize = params.length;
            int remainSize = totalSize;
            for (Object[] param : params) {
                for (int i = 0, len = param.length; i < len; i++) {
                    preparedStatement.setObject(i + 1, param[i]);
                }
                preparedStatement.addBatch();
                currentBatchSize++;

                if (currentBatchSize == batchSize) {
                    preparedStatement.executeBatch();
                    getConnection().commit();
                    currentBatchSize = 0;
                    remainSize = remainSize - batchSize;
                    long end = System.currentTimeMillis();
                    log.debug("[总数：" + totalSize + "，剩余：" + remainSize + "]插入" + batchSize + "条数据，耗时：" + ((end - start) / 1000.0) + "s");
                    start = end;
                }
            }

            if (currentBatchSize > 0) {
                preparedStatement.executeBatch();
                getConnection().commit();
                remainSize = remainSize - currentBatchSize;
                long end = System.currentTimeMillis();
                log.debug("[总数：" + totalSize + "，剩余：" + remainSize + "]插入" + batchSize + "条数据，耗时：" + ((end - start) / 1000.0) + "s");
            }
            log.debug("[总数：" + totalSize + "]插入总耗时：" + ((System.currentTimeMillis() - totalStart) / 1000.0) + "s");
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        } finally {
            free(null, preparedStatement, null);
        }
    }
}
