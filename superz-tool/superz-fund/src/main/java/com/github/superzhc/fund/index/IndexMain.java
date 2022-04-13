package com.github.superzhc.fund.index;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author superz
 * @create 2022/4/13 10:24
 **/
public class IndexMain {
    private static final Logger log = LoggerFactory.getLogger(IndexMain.class);

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        String sql = "select * from index_basic";

        try (JdbcHelper jdbcHelper = new JdbcHelper(url, username, password)) {
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            log.debug("查询语句：{}", sql);
            pstmt = jdbcHelper.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();

            Table table = Table.read().db(rs);
            System.out.println(table.printAll());

            jdbcHelper.free(null, pstmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
