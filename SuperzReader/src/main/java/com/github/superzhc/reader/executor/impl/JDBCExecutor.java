package com.github.superzhc.reader.executor.impl;

import com.github.superzhc.reader.datasource.impl.JDBCDatasource;
import com.github.superzhc.reader.param.impl.JDBCParam;
import com.github.superzhc.reader.util.PlaceholderResolver;

import java.sql.*;

/**
 * @author superz
 * @create 2021/8/17 16:53
 */
public class JDBCExecutor {
    private JDBCDatasource dataSource;
    private JDBCParam param;

    public JDBCExecutor(JDBCDatasource dataSource, JDBCParam param) {
        this.dataSource = dataSource;
        this.param = param;
    }

    public void execute() {
        Connection connection = null;
        Statement stmt = null;
        try {
            // 加载驱动程序
            Class.forName(dataSource.getDriver());

            // 获取数据库连接
            connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.convert());

            String sql = PlaceholderResolver.getDefaultResolver().resolveByMap(param.getSql(), param.getParams());

            // 创建Statement
            stmt = connection.createStatement();
            ResultSet rs1 = stmt.executeQuery(sql);

            // 遍历结果集
            while (rs1.next()) {
                // doing something
                System.out.println(rs1.getDate(1));
            }

            // 关闭资源
            rs1.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
