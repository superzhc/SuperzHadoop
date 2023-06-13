package com.github.superzhc.db.client.jdbi3;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;

public class Jdbi3Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306?useSSL=false";
        String username = "root";
        String password = "123456";

        final Jdbi jdbi = Jdbi.create(url, username, password);

        // 由于Handle会持有一个打开的数据库连接，因此必须注意确保每个Handle在完成后都关闭。无法关闭的Handles将最终因为连接过多淹没您的数据库，或者耗尽您的连接池。
//        try (Handle handle = jdbi.open()) {
//            // do something
//        }

        // 如果操作有返回结果使用如下方式：
//        List<String> databases = jdbi.withHandle(handle -> {
//            return handle.createQuery("show databases").mapTo(String.class).list();
//        });
//        System.out.println(databases);

//        List<String> tables = jdbi.withHandle(handle -> {
//            handle.execute("use xgit");
//            return handle.createQuery("show tables").mapTo(String.class).list();
//        });
//        System.out.println(tables);

        List<Map<String, Object>> data = jdbi.withHandle(handle -> {
            handle.execute("use xgit");
            return handle.createQuery("select * from test_20230601_001")
                    .mapToMap()
                    .list();
        });
        System.out.println(data);

//        // 如果操作不需要返回结果使用如下方式：
//        jdbi.useHandle(handle -> {
//            // do something
//        });
    }
}
