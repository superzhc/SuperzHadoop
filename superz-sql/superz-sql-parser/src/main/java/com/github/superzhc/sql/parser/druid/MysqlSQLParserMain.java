package com.github.superzhc.sql.parser.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
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
        String sql2_1 = "insert into t1 values(1,'v1','v2')";
        String sql2_2 = "insert into t1(id,c1,c2) values(1,'v1','v2')";
        String sql2_3 = "insert into t1 select * from t2";
        String sql2_4 = "insert into t1 select x,y,z from t2";
        String sql2_5 = "insert into t1(a,b,c) select * from t2";
        String sql5 = "create table t1(id int primary key not null,x varchar(255),y int);select * from t1;select * from (select * from t1) t2;insert into t3 select * from t1;update t2 set x='1' where y=2;delete from t4 where id=0;";

        // 解析 SQL 语句，每一个 SQLStatement 代表一条完整的 SQL 语句
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql1_2, DB_TYPE);
        for (SQLStatement sqlStatement : statementList) {
            SQLStatementUsage(sqlStatement);
        }
    }

    private static void SQLStatementUsage(SQLStatement sqlStatement) {
        /* 最常用的Statement是SELECT/UPDATE/DELETE/INSERT */
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
            SQLSelectStatementUsage(sqlSelectStatement);
        } else if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatement;
            SQLInsertStatementUsage(sqlInsertStatement);
        } else if (sqlStatement instanceof SQLUpdateStatement) {
            SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatement;

            // TODO

        } else if (sqlStatement instanceof SQLDeleteStatement) {
            SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;

            // TODO

        }
        /* 建表、建数据库、建索引的语句 */
        // 建表
        else if (sqlStatement instanceof SQLCreateTableStatement) {

        } else {
            log.error("TODO：{}", sqlStatement.getClass().getName());
        }
    }

    private static void SQLSelectStatementUsage(SQLSelectStatement statement) {
        SQLSelect sqlSelect = statement.getSelect();

//        SQLSelectQuery query = sqlSelect.getQuery();
        SQLSelectQueryBlock sqlSelectQueryBlock = sqlSelect.getQueryBlock();
        // 获取查询字段
        List<SQLSelectItem> columns = sqlSelectQueryBlock.getSelectList();
        for (SQLSelectItem column : columns) {
            System.out.println("字段列：" + SQLUtils.toSQLString(column, DB_TYPE));
        }

        // 获取查询的表
        SQLTableSource table = sqlSelectQueryBlock.getFrom();
        // SQLTableSource 有多种实现，常见的实现SQLExprTableSource（from的表）、SQLJoinTableSource（join的表）、SQLSubqueryTableSource（子查询的表）
//        System.out.println("表名：" + SQLUtils.toSQLString(table, DB_TYPE));
        System.out.println("表名：" + table.getAlias());

        if (table instanceof SQLSubqueryTableSource) {
//            SQLSelect subSQLSelect = ((SQLSubqueryTableSource) table).getSelect();
//            // 重复上面的操作
//            System.out.println("sub query:" + SQLUtils.toSQLString(subSQLSelect, DB_TYPE));
        }

        /*
         WITH...AS 语句，若无，则该值为null
         形如：
         WITH RECURSIVE ancestors AS (
            SELECT *
            FROM org
            UNION
            SELECT f.*
            FROM org f, ancestors a
            WHERE f.id = a.parent_id
        )
        SELECT *
        FROM ancestors;
         */
//        SQLWithSubqueryClause withSubQuery = sqlSelect.getWithSubQuery();
//        System.out.println("with...as query:" + (null == withSubQuery ? "null" : SQLUtils.toSQLString(withSubQuery, DB_TYPE)));
    }

    private static void SQLInsertStatementUsage(SQLInsertStatement statement) {
        SQLTableSource to = statement.getTableSource();
        System.out.println("插入表：" + SQLUtils.toSQLString(to, DB_TYPE));

        // 插入的列，若未提供则如何获取列的元数据
        List<SQLExpr> exprs = statement.getColumns();
        for (SQLExpr expr : exprs) {
            System.out.println(SQLUtils.toSQLString(expr, DB_TYPE));
        }

        // insert into ... select ... 语句，若非该形式的新增，则 SQLSelect 为 null
        SQLSelect sqlSelect = statement.getQuery();
        System.out.println("select 子句：" + (null == sqlSelect ? "null" : SQLUtils.toSQLString(sqlSelect, DB_TYPE)));
    }

    private static void SchemaStatVisitorUsage(SQLStatement sqlStatement) {
        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(DB_TYPE);
        sqlStatement.accept(visitor);

        System.out.println(visitor.getColumns());
        System.out.println(visitor.getTables());
    }
}
