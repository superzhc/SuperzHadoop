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
        TableInfo tableInfo = new TableInfo(SQLUtils.toSQLString(sqlSelect));

        SQLSelectQuery query = sqlSelect.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) query;

            // 获取查询的表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            TableInfo sourceTableInfo = SQLTablSourceUsage.usage(table);
            tableInfo.addTables(sourceTableInfo.getTables());

            // 获取字段
            List<SQLSelectItem> columns = sqlSelectQueryBlock.getSelectList();
            if (null != columns && columns.size() > 0) {
                for (SQLSelectItem column : columns) {
                    // log.debug("字段列：{}", SQLUtils.toSQLString(column));
                    String columnName = SQLUtils.toSQLString(column.getExpr());
                    if ("*".equals(columnName)) {
                        if (sourceTableInfo.getFields().size() == 0) {
                            TableField field = new TableField(sourceTableInfo.getId(), "*");
                            tableInfo.addField(field);
                        } else {
                            tableInfo.addFields(sourceTableInfo.getFields());
                        }
                    } else {
                        TableField field = new TableField(tableInfo.getId(), columnName);
                        tableInfo.addField(field);
                    }
                }
            } else {
                // log.warn("查询字段列为空");
                tableInfo.addFields(sourceTableInfo.getFields());
            }
        } else if (query instanceof SQLUnionQuery) {
            log.warn("尚未实现 SQLUnionQuery 分析：{}", SQLUtils.toSQLString(query));
        } else {
            log.error("当前类型{}尚未实现分析：{}", query.getClass().getName(), SQLUtils.toSQLString(query));
        }
        return tableInfo;
    }

    public static void main(String[] args) {
        String sql = "select * from t1 a inner join t2 b on a.id=b.id where id=1 and a.name='superz'";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        usage((SQLSelectStatement) statement);
    }
}
