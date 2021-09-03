package com.github.superzhc.db.transform;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author superz
 * @create 2021/8/5 10:28
 */
public abstract class Transform2SQL extends Transform {
    private QueryRunner queryRunner = null;

    public Transform2SQL() {
        initQueryRunner();
    }

    protected void initQueryRunner() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/superz?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";
        DbUtils.loadDriver(driver);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        DataSource ds = new HikariDataSource(config);
        this.queryRunner = new QueryRunner(ds);
    }

    @Override
    public int transform(String data) {
        try {
            String sql = sql();
            Object[] params = params(data);
            return queryRunner.update(sql, params);
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public int[] transformBatch(String[] datas) {
        try {
            String sql = sql();
            int size = datas.length;
            Object[][] paramsArr = new Object[size][];
            for (int i = 0; i < size; i++) {
                paramsArr[i] = params(datas[i]);
            }
            return queryRunner.batch(sql, paramsArr);
        } catch (Exception ex) {
            return new int[]{-1};
        }
    }

    public abstract String sql();

    public abstract Object[] params(String data);
}
