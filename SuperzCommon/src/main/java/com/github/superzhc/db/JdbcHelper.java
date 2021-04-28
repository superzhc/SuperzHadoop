package com.github.superzhc.db;

import com.github.superzhc.util.StringUtils;
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
        this(Driver.match(url).fullClassName(), url, username, password);
    }

    public JdbcHelper(String driver, String url, String username, String password) {
        dbConfig = new DBConfig(driver, url, username, password);
    }

    private Connection getConnection() {
        if (null == conn) {
            try {
                Class.forName(dbConfig.driver);
                Properties info = new Properties();

                if (StringUtils.isNotBlank(dbConfig.username)) {
                    info.put("user", dbConfig.username);
                }
                if (StringUtils.isNotBlank(dbConfig.password)) {
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

    public <T> T queryOne(String sql, Object... params) {
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
    public int[] batchUpdate(List<String> sqlList) {
        int[] result = new int[]{};
        Statement statement = null;
        try {
            getConnection().setAutoCommit(false);
            statement = getConnection().createStatement();
            for (String sql : sqlList) {
                statement.addBatch(sql);
            }
            result = statement.executeBatch();
            getConnection().commit();
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                throw new ExceptionInInitializerError(e1);
            }
            throw new ExceptionInInitializerError(e);
        } finally {
            free(null, statement, null);
        }
        return result;
    }

    public int[] batchUpdate(String sql, List<Map<Integer, Object>> params) {
        int[] result = new int[]{};
        PreparedStatement preparedStatement = null;
        try {
            getConnection().setAutoCommit(false);
            preparedStatement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (Map<Integer, Object> param : params) {
                for (Map.Entry<Integer, Object> entry : param.entrySet()) {
                    preparedStatement.setObject(entry.getKey(), entry.getValue());
                }
                preparedStatement.addBatch();
            }
            result = preparedStatement.executeBatch();
            getConnection().commit();
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                throw new ExceptionInInitializerError(e1);
            }
            throw new ExceptionInInitializerError(e);
        } finally {
            free(null, preparedStatement, null);
        }
        return result;
    }
}
