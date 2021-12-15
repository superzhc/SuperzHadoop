package com.github.superzhc.common.jdbc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 2020年11月04日 superz add
 */
public class JdbcHelper implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(JdbcHelper.class);

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
        this(null == Driver.match(url) ? null : Driver.match(url).fullClassName(), url, username, password);
    }

    public JdbcHelper(String driver, String url, String username, String password) {
        dbConfig = new DBConfig(driver, url, username, password);
    }

    private Connection getConnection() {
        if (null == conn) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放statement
     *
     * @param statement
     */
    private void freeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放resultset
     *
     * @param rs
     */
    private void freeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
        if (rs != null) {
            freeResultSet(rs);
        }
        if (statement != null) {
            freeStatement(statement);
        }
        if (conn != null) {
            freeConnection(conn);
        }
    }

    @Override
    public void close() {
        free(conn, null, null);
    }

    /**
     * 判断表是否存在
     *
     * @param schema
     * @return
     */
    public boolean exist(String schema) {
        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            rs = metaData.getTables(conn.getCatalog(), conn.getSchema(), schema, new String[]{"TABLE"});
            return rs.next();
        } catch (Exception e) {
            return false;
        } finally {
            free(null, null, rs);
        }
    }

    public String[] tables() {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            List<String> result = new ArrayList<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(connection.getCatalog(), connection.getSchema(), "%", new String[]{"TABLE"});
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

    public String[] columns(String schema) {
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            List<String> result = new ArrayList<>();

            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getColumns(connection.getCatalog(), connection.getSchema(), schema, "%");
            while (rs.next()) {
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

    public int ddlExecute(String sql) {
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

    public int dmlExecute(String schema, String[] columns, Object... params) {
        StringBuilder columnsSb = new StringBuilder();
        StringBuilder placeholdSb = new StringBuilder();
        for (String column : columns) {
            columnsSb.append(",").append(column);
            placeholdSb.append(",?");
        }

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", schema, columnsSb.substring(1), placeholdSb.substring(1));
        return dmlExecute(sql, params);
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
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
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

    public void show(String sql, Object... params) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            ResultSetUtils.print(rs);
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
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Object result = null;
        try {
            pstmt = getConnection().prepareStatement(sql);
            if (null != params && params.length != 0) {
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

//    public int[] batchUpdate(String sql, List<Map<Integer, Object>> params) {
//        int[] result = new int[]{};
//        PreparedStatement preparedStatement = null;
//        try {
//            getConnection().setAutoCommit(false);
//            preparedStatement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//            for (Map<Integer, Object> param : params) {
//                for (Map.Entry<Integer, Object> entry : param.entrySet()) {
//                    preparedStatement.setObject(entry.getKey(), entry.getValue());
//                }
//                preparedStatement.addBatch();
//            }
//            result = preparedStatement.executeBatch();
//            getConnection().commit();
//        } catch (SQLException e) {
//            try {
//                getConnection().rollback();
//            } catch (SQLException e1) {
//                throw new ExceptionInInitializerError(e1);
//            }
//            throw new ExceptionInInitializerError(e);
//        } finally {
//            free(null, preparedStatement, null);
//        }
//        return result;
//    }

    public void batchUpdate(String schema, String[] columns, List<List<Object>> params) {
        batchUpdate(schema, columns, params, params.size());
    }

    public void batchUpdate(String schema, String[] columns, List<List<Object>> params, Integer batchSize) {

        StringBuilder columnsSb = new StringBuilder();
        StringBuilder placeholdSb = new StringBuilder();
        for (String column : columns) {
            columnsSb.append(",").append(column);
            placeholdSb.append(",?");
        }

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", schema, columnsSb.substring(1), placeholdSb.substring(1));
        batchUpdate(sql, params, batchSize);
    }

    public void batchUpdate(String sql, List<List<Object>> params) {
        batchUpdate(sql, params, params.size());
    }

    public void batchUpdate(String sql, List<List<Object>> params, Integer batchSize) {
        log.debug("batch sql:" + sql);
        log.debug("batch size:" + batchSize);
        if (null == params || params.size() == 0) {
            log.debug("no data");
            return;
        }

        PreparedStatement preparedStatement = null;
        try {
            getConnection().setAutoCommit(false);
            preparedStatement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            long start = System.currentTimeMillis();
            int currentBatchSize = 0;
            int totalSize = params.size();
            int remainSize = totalSize;
            for (List<Object> param : params) {
                for (int i = 0, len = param.size(); i < len; i++) {
                    preparedStatement.setObject(i + 1, param.get(i));
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
