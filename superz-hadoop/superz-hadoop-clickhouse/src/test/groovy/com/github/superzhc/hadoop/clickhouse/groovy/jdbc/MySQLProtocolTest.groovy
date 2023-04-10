package com.github.superzhc.hadoop.clickhouse.groovy.jdbc

/**
 * @author superz
 * @create 2023/4/10 17:13
 * */
class MySQLProtocolTest {
    // clickhouse 支持mysql协议，默认使用的端口号是9004，配置项：<mysql_port>9004</mysql_port>
    // 使用JDBC需要引入驱动文件
    String url="jdbc:mysql://127.0.0.1:9004?useSSL=false"
}
