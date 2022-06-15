package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/15 10:48
 **/
public class SQLSelectStatementUsage {
    private static final Logger log = LoggerFactory.getLogger(SQLSelectStatementUsage.class);

    public static void usage(SQLSelectStatement statement) {
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

        SQLSelect sqlSelect = statement.getSelect();
        usage(sqlSelect);
    }

    public static void usage(SQLSelect sqlSelect) {
        SQLSelectQuery query = sqlSelect.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) query;

            // 获取字段
            List<SQLSelectItem> columns = sqlSelectQueryBlock.getSelectList();
            if (null != columns && columns.size() > 0) {
                for (SQLSelectItem column : columns) {
                    log.debug("字段列：{}", SQLUtils.toSQLString(column));
                }
            } else {
                log.warn("字段列为空");
            }

            SQLExpr where = sqlSelectQueryBlock.getWhere();
            if (null != where) {
                log.debug("条件：{}", SQLUtils.toSQLString(where));
            }

            SQLSelectGroupByClause sqlSelectGroupByClause = sqlSelectQueryBlock.getGroupBy();
            if (null != sqlSelectGroupByClause) {
                List<SQLExpr> items = sqlSelectGroupByClause.getItems();
                for (SQLExpr item : items) {
                    log.debug("GroupBy 列：{}", SQLUtils.toSQLString(item));
                }
            }

            // SQLTableSource 有多种实现，常见的实现SQLExprTableSource（from的表）、SQLJoinTableSource（join的表）、SQLSubqueryTableSource（子查询的表）
            // 获取查询的表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            SQLTablSourceUsage(table);
        } else if (query instanceof SQLUnionQuery) {
            log.warn("尚未实现 SQLUnionQuery 分析：{}", SQLUtils.toSQLString(query));
        } else {
            log.error("当前类型{}尚未实现分析：{}", query.getClass().getName(), SQLUtils.toSQLString(query));
        }
    }

    private static void SQLTablSourceUsage(SQLTableSource table) {
        if (table instanceof SQLExprTableSource) {
            log.debug("表名：{}", SQLUtils.toSQLString(table));
        } else if (table instanceof SQLSubqueryTableSource/*子查询作为表*/) {
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) table;
            log.debug("{}子查询语句：{}",
                    (null == sqlSubqueryTableSource.getAlias() ? "" : "表名：" + sqlSubqueryTableSource.getAlias())
                    , SQLUtils.toSQLString(sqlSubqueryTableSource)
            );
            usage(sqlSubqueryTableSource.getSelect());
        } else if (table instanceof SQLJoinTableSource /*Join查询*/) {
            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) table;
            log.debug("{}Join查询语句：{}",
                    (null == sqlJoinTableSource.getAlias() ? "" : "表名：" + sqlJoinTableSource.getAlias())
                    , SQLUtils.toSQLString(sqlJoinTableSource)
            );
            SQLTableSource left = sqlJoinTableSource.getLeft();
            SQLTablSourceUsage(left);
            SQLTableSource right = sqlJoinTableSource.getRight();
            SQLTablSourceUsage(right);
//            SQLJoinTableSource.JoinType joinType = sqlJoinTableSource.getJoinType();
        } else {
            log.error("当前表类型{}尚未实现分析：{}", table.getClass().getName(), SQLUtils.toSQLString(table));
        }
    }

    public static void main(String[] args) {
        String sql = "select * from t1 where id=1 and name='superz'";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        usage((SQLSelectStatement) statement);
    }
}
