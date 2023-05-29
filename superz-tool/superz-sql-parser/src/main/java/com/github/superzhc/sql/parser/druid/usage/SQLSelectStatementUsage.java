package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import com.github.superzhc.sql.parser.druid.lineage.TableField;
import com.github.superzhc.sql.parser.druid.lineage.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/15 10:48
 **/
public class SQLSelectStatementUsage {
    private static final Logger log = LoggerFactory.getLogger(SQLSelectStatementUsage.class);

    public static TableInfo usage(SQLSelectStatement statement) {
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
        return usage(sqlSelect);
    }

    public static TableInfo usage(SQLSelect sqlSelect) {
        SQLSelectQuery query = sqlSelect.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) query;

            // 获取查询的表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            TableInfo sourceTableInfo = SQLTableSourceUsage.usage(table);

            TableInfo tableInfo = sourceTableInfo.setExpr(SQLUtils.toSQLString(sqlSelect));

            // 获取字段
            List<SQLSelectItem> columns = sqlSelectQueryBlock.getSelectList();
            if (null != columns && columns.size() > 0) {

                // 窄化：若有选择列，则代表从表中选取有限的列，这是一个窄化的过程
                List<TableField> fields = tableInfo.clearFields();

                for (SQLSelectItem column : columns) {
                    String columnName = SQLUtils.toSQLString(column.getExpr());
                    if ("*".equals(columnName)) {
                        tableInfo.addFields(fields);
                    } else {
                        TableField field = new TableField(tableInfo.getId(), columnName);
                        field.setTable(tableInfo.getTable());
                        field.setAlias(column.getAlias());
                        tableInfo.addField(field);
                    }
                }
            }

            log.debug("{}", tableInfo);
            return tableInfo;
        } else if (query instanceof SQLUnionQuery) {
            log.warn("尚未实现 SQLUnionQuery 分析：{}", SQLUtils.toSQLString(query));
        } else {
            log.error("当前类型{}尚未实现分析：{}", query.getClass().getName(), SQLUtils.toSQLString(query));
        }
        return null;
    }

    public static void main(String[] args) {
        String sql = "select * from t1 a inner join t2 b on a.id=b.id where id=1 and a.name='superz'";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        SQLStatementUsage.usage(statement);
    }
}
