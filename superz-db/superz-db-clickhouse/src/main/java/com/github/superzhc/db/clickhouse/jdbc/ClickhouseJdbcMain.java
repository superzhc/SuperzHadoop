package com.github.superzhc.db.clickhouse.jdbc;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ClickhouseJdbcMain {
    public static final String DRIVER = "com.clickhouse.jdbc.ClickHouseDriver";

    public static void main(String[] args) throws Exception {
        String url = "jdbc:ch:http://10.3.2.99:8123/tpcds";
        Properties properties = new Properties();
        // properties.setProperty("ssl", "true");
        // properties.setProperty("sslmode", "NONE"); // NONE to trust all servers; STRICT for trusted only
        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);

        final Jdbi jdbi = Jdbi.create(dataSource);

        /*创建数据库*/
//        jdbi.useHandle(handle -> {
//             handle.execute("CREATE DATABASE tpcds /*ON CLUSTER company_cluster*/");
////            handle.execute("DROP DATABASE tpcds");
//        });

//        List<String> databases=jdbi.withHandle(handle -> {
//            return handle.createQuery("show databases")
//                    .mapTo(String.class)
//                    .list()
//                    ;
//        });
//        System.out.println(databases);

        List<Map<String, Object>> data = jdbi.withHandle(handle -> {
            String sql = "select * from call_center";

            // 使用sum函数，大小写敏感造成报错，why？？？
            sql="with customer_total_return as\n" +
                    "(select sr_customer_sk as ctr_customer_sk\n" +
                    ",sr_store_sk as ctr_store_sk\n" +
                    ",sum(SR_FEE) as ctr_total_return\n" +
                    "from store_returns\n" +
                    ",date_dim\n" +
                    "where sr_returned_date_sk = d_date_sk\n" +
                    "and d_year =2000\n" +
                    "group by sr_customer_sk\n" +
                    ",sr_store_sk)\n" +
                    " select top 100 c_customer_id\n" +
                    "from customer_total_return ctr1\n" +
                    ",store\n" +
                    ",customer\n" +
                    "where ctr1.ctr_total_return > (select avg(ctr_total_return)*1.2\n" +
                    "from customer_total_return ctr2\n" +
                    "where ctr1.ctr_store_sk = ctr2.ctr_store_sk)\n" +
                    "and s_store_sk = ctr1.ctr_store_sk\n" +
                    "and s_state = 'TN'\n" +
                    "and ctr1.ctr_customer_sk = c_customer_sk\n" +
                    "order by c_customer_id";

            List<Map<String, Object>> lst = handle.createQuery(sql)
                    .mapToMap()
                    .list();

            return lst;
        });
        System.out.println(data);

    }
}
