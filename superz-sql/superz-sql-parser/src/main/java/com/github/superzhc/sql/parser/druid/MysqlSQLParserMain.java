package com.github.superzhc.sql.parser.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.github.superzhc.sql.parser.druid.usage.SQLStatementUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/14 14:09
 **/
public class MysqlSQLParserMain {
    private static final Logger log = LoggerFactory.getLogger(MysqlSQLParserMain.class);

    private static final DbType DB_TYPE = JdbcConstants.MYSQL;

    public static void main(String[] args) {
        String sql1_1 = "select a.x,a.y,a.z from test a where a.id=3";
        String sql1_2 = "select t2.x m,t2.y n from (select a.x,a.y,a.z from test a where a.id=3) t2";
        String sql1_3 = "select\n" +
                " \tcase when a.kamut=1 and b.teur IS null\n" +
                " \t\t\t then 'no locks'\n" +
                " \t\t when a.kamut=1\n" +
                " \t\t\tthen b.teur\n" +
                " \telse 'locks'\n" +
                " \tend teur\n" +
                " from tbl a left join TT b on (a.key=b.key)";


        String sql2_1 = "insert into t1 values(1,'v1','v2')";
        String sql2_2 = "insert into t1(id,c1,c2) values(1,'v1','v2')";
        String sql2_3 = "insert into t1 select * from t2";
        String sql2_4 = "insert into t1 select x,y,z from t2";
        String sql2_5 = "insert into t1(a,b,c) select * from t2";
        String sql5 = "create table t1(id int primary key not null,x varchar(255),y int);select * from t1;select * from (select * from t1) t2;insert into t3 select * from t1;update t2 set x='1' where y=2;delete from t4 where id=0;";

        // 解析 SQL 语句，每一个 SQLStatement 代表一条完整的 SQL 语句
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql1_3, DB_TYPE);
        for (SQLStatement sqlStatement : statementList) {
            SQLStatementUsage.usage(sqlStatement);
        }
    }

    private static void SchemaStatVisitorUsage(SQLStatement sqlStatement) {
        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(DB_TYPE);
        sqlStatement.accept(visitor);

        System.out.println(visitor.getColumns());
        System.out.println(visitor.getTables());
    }
}
