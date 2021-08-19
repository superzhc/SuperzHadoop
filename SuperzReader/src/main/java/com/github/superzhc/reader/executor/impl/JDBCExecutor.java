package com.github.superzhc.reader.executor.impl;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.datasource.impl.JDBCDatasource;
import com.github.superzhc.reader.executor.Executor;
import com.github.superzhc.reader.param.impl.JDBCParam;
import com.github.superzhc.reader.util.PlaceholderResolver;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 16:53
 */
@Slf4j
public class JDBCExecutor extends Executor {
    private JDBCDatasource dataSource;
    private JDBCParam param;

    public JDBCExecutor(String datasourceConfig, String paramConfig) {
        super(datasourceConfig, paramConfig);
        this.dataSource = new JDBCDatasource(JSON.parseObject(datasourceConfig).getInnerMap());
        this.param = JSON.parseObject(paramConfig, JDBCParam.class);
    }

    @Override
    public ResultT execute(Map<String, Object> values) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // 加载驱动程序
            Class.forName(dataSource.getDriver());

            // 获取数据库连接
            connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.info());

            String sql = PlaceholderResolver.getDefaultResolver().resolveByMap(param.getSql(), values);
            log.info("请求SQL：{}", sql);

            // 创建Statement
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Map<String, Object>> list = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = rs.getObject(cols_name);
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
            // 关闭资源
            rs.close();

            return ResultT.success(list);
        } catch (SQLException ex) {
            /*ex.printStackTrace();*/
            return ResultT.fail(ex.getMessage());
        } catch (ClassNotFoundException e) {
            /*e.printStackTrace();*/
            return ResultT.fail(e.getMessage());
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
