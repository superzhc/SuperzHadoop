package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.*;
import com.github.superzhc.sql.parser.druid.lineage.TableField;
import com.github.superzhc.sql.parser.druid.lineage.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/6/16 16:14
 **/
public class SQLTableSourceUsage {
    private static final Logger log = LoggerFactory.getLogger(SQLTableSourceUsage.class);

    /**
     * SQLTableSource 有多种实现，常见的实现SQLExprTableSource（from的表）、SQLJoinTableSource（join的表）、SQLSubqueryTableSource（子查询的表）
     *
     * @param table
     * @return
     */
    public static TableInfo usage(SQLTableSource table) {
        if (table instanceof SQLExprTableSource) {
            /**
             * 形如：
             * 1. table1
             * 2. table1 t1
             * 3. table1 as t1
             */
            SQLExprTableSource tableSource = (SQLExprTableSource) table;
            String tableName=tableSource.getTableName();
            String alias=tableSource.getAlias();

            TableInfo tableInfo = new TableInfo(SQLUtils.toSQLString(table))
                    .setTable(tableSource.getTableName())
                    .setAlias(tableSource.getAlias());

            log.debug("{}", tableInfo);

            return tableInfo;
        } else if (table instanceof SQLSubqueryTableSource) {
            /**
             * 子查询作为表
             * 形如：
             * 1. (select * from t1) t2
             * 2. (select c1,c2,c3 from t1) as t2
             */
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) table;
            String alias=sqlSubqueryTableSource.getAlias();
            SQLSelect sqlSelect = sqlSubqueryTableSource.getSelect();
            TableInfo subTableInfo = SQLSelectStatementUsage.usage(sqlSelect);

            TableInfo tableInfo = new TableInfo(SQLUtils.toSQLString(table));
            tableInfo.setAlias(sqlSubqueryTableSource.getAlias());
            // 因为同一个 TableInfo 的 table、dependenceTables 互斥，即只有一个有实际的意义
            tableInfo.addDependenceTable(subTableInfo.getTable()).addDependenceTables(subTableInfo.getDependenceTables());
            tableInfo.addFields(subTableInfo.getFields());

            log.debug("{}", tableInfo);

            return tableInfo;
        } else if (table instanceof SQLJoinTableSource) {
            /**
             * Join 表
             *
             * 例如：
             * table1 a inner join table2 b 会被解析成 select * from table1 a inner join table2 b 这样的子句查询
             *
             * 合并左右表
             *
             * join 模式下，得到的字段是左右两个表提供的字段和
             */
            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) table;

            // 左表
            SQLTableSource left = sqlJoinTableSource.getLeft();
            TableInfo leftTableInfo = usage(left);
            if (leftTableInfo.getFields().size() == 0) {
                TableField leftField = new TableField(leftTableInfo.getId(), "*");
                leftField.setTable(leftTableInfo.getTable());
                leftTableInfo.addField(leftField);
            }

            // 右表
            SQLTableSource right = sqlJoinTableSource.getRight();
            TableInfo rightTableInfo = usage(right);
            if (rightTableInfo.getFields().size() == 0) {
                TableField rightField = new TableField(rightTableInfo.getId(), "*");
                rightField.setTable(rightTableInfo.getTable());
                rightTableInfo.addField(rightField);
            }

            // SQLJoinTableSource.JoinType joinType = sqlJoinTableSource.getJoinType();

            TableInfo tableInfo = new TableInfo(SQLUtils.toSQLString(sqlJoinTableSource));
            tableInfo.addDependenceTable(leftTableInfo.getTable()).addDependenceTables(leftTableInfo.getDependenceTables())
                    .addDependenceTable(rightTableInfo.getTable()).addDependenceTables(rightTableInfo.getDependenceTables());
            tableInfo.addFields(leftTableInfo.getFields()).addFields(rightTableInfo.getFields());

            log.debug("{}", tableInfo);

            return tableInfo;
        } else {
            log.error("当前表类型{}尚未实现分析：{}", table.getClass().getName(), SQLUtils.toSQLString(table));
        }
        return new TableInfo();
    }
}
