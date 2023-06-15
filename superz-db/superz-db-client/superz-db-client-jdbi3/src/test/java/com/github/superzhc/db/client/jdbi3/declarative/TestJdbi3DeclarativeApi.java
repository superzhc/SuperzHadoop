package com.github.superzhc.db.client.jdbi3.declarative;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestJdbi3DeclarativeApi {
    Jdbi jdbi = null;

    @Before
    public void setUp() throws Exception {
        jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/tpcds?useSSL=false", "root", "123456");
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Test
    public void test() {
        List<Map<String, Object>> data = jdbi.withExtension(UserDao.class, dao -> {
            return dao.listData();
        });
        System.out.println(data);
    }
}
