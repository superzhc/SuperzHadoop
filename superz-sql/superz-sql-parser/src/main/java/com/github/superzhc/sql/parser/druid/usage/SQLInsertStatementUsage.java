package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/15 14:49
 **/
public class SQLInsertStatementUsage {
    public static void usage(SQLInsertStatement statement){
        SQLTableSource to = statement.getTableSource();
        System.out.println("插入表：" + SQLUtils.toSQLString(to));

        // 插入的列，若未提供则如何获取列的元数据
        List<SQLExpr> exprs = statement.getColumns();
        for (SQLExpr expr : exprs) {
            System.out.println(SQLUtils.toSQLString(expr));
        }

        // insert into ... select ... 语句，若非该形式的新增，则 SQLSelect 为 null
        SQLSelect sqlSelect = statement.getQuery();
        System.out.println("select 子句：" + (null == sqlSelect ? "null" : SQLUtils.toSQLString(sqlSelect)));
    }
}
