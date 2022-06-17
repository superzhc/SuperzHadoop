package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLStatementUsage {
    static final Logger log = LoggerFactory.getLogger(SQLStatementUsage.class);

    public static void usage(SQLStatement sqlStatement) {
        log.info("SQL 分析语句：\n{}", SQLUtils.toSQLString(sqlStatement));
        Object result = null;

        /* 最常用的Statement是SELECT/UPDATE/DELETE/INSERT */
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
            result = SQLSelectStatementUsage.usage(sqlSelectStatement);
        } else if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatement;
            SQLInsertStatementUsage.usage(sqlInsertStatement);
        }
//        else if (sqlStatement instanceof SQLUpdateStatement) {
//            SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatement;
//
//            // TODO
//
//        } else if (sqlStatement instanceof SQLDeleteStatement) {
//            SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;
//
//            // TODO
//
//        }
        /* 建表、建数据库、建索引的语句 */
        // 建表
        else if (sqlStatement instanceof SQLCreateTableStatement) {
            SQLCreateTableStatement sqlCreateTableStatement = (SQLCreateTableStatement) sqlStatement;
            SQLCreateTableStatementUsage.usage(sqlCreateTableStatement);
        } else {
            result = String.format("TODO：%s", sqlStatement.getClass().getName());
        }
        log.info("SQL 分析结果：{}", result);
    }
}
