package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.*;
import com.github.superzhc.sql.parser.druid.lineage.DataLineage;
import com.github.superzhc.sql.parser.druid.lineage.TableField;
import com.github.superzhc.sql.parser.druid.lineage.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author superz
 * @create 2022/6/16 16:14
 **/
public class SQLTablSourceUsage {
    private static final Logger log = LoggerFactory.getLogger(SQLTablSourceUsage.class);

    /**
     * SQLTableSource 有多种实现，常见的实现SQLExprTableSource（from的表）、SQLJoinTableSource（join的表）、SQLSubqueryTableSource（子查询的表）
     *
     * @param table
     * @return
     */
    public static TableInfo usage(SQLTableSource table) {
        if (table instanceof SQLExprTableSource) {
            /**
             * 表
             */

            SQLExprTableSource tableSource = (SQLExprTableSource) table;
            //log.debug("表名：{}", SQLUtils.toSQLString(table));
            String tableName = tableSource.getTableName();
            TableInfo tableInfo = new TableInfo(SQLUtils.toSQLString(table)).addTable(tableName);
            log.debug("{}", tableInfo);
            return tableInfo;
        } else if (table instanceof SQLSubqueryTableSource) {
            /**
             * 子查询作为表
             *
             * 获取子查询使用的表
             */

            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) table;
//            log.debug("{}子查询语句：{}",
//                    (null == sqlSubqueryTableSource.getAlias() ? "" : "表名：" + sqlSubqueryTableSource.getAlias())
//                    , SQLUtils.toSQLString(sqlSubqueryTableSource)
//            );

            SQLSelect sqlSelect = sqlSubqueryTableSource.getSelect();
            TableInfo tableInfo = SQLSelectStatementUsage.usage(sqlSelect);
            log.debug("{}", tableInfo);
            return tableInfo;
        } else if (table instanceof SQLJoinTableSource) {
            /**
             * Join 表
             *
             * 合并左右表
             *
             * join 模式下，得到的字段是左右两个表提供的字段和
             */

            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) table;
//            log.debug("{}Join查询语句：{}",
//                    (null == sqlJoinTableSource.getAlias() ? "" : "表名：" + sqlJoinTableSource.getAlias())
//                    , SQLUtils.toSQLString(sqlJoinTableSource)
//            );

            // 左表
            SQLTableSource left = sqlJoinTableSource.getLeft();
            TableInfo leftTableInfo = usage(left);
            if (leftTableInfo.getFields().size() == 0) {
                TableField leftField = new TableField(leftTableInfo.getId(), "*");
                leftTableInfo.addField(leftField);
            }

            // 右表
            SQLTableSource right = sqlJoinTableSource.getRight();
            TableInfo rightTableInfo = usage(right);
            if (rightTableInfo.getFields().size() == 0) {
                TableField rightField = new TableField(rightTableInfo.getId(), "*");
                rightTableInfo.addField(rightField);
            }

//            SQLJoinTableSource.JoinType joinType = sqlJoinTableSource.getJoinType();

            TableInfo tableinfo = new TableInfo(SQLUtils.toSQLString(sqlJoinTableSource));
            tableinfo.addFields(leftTableInfo.getFields()).addFields(rightTableInfo.getFields());
            tableinfo.addTables(leftTableInfo.getTables()).addTables(rightTableInfo.getTables());
            log.debug("{}", tableinfo);
            return tableinfo;
        } else {
            log.error("当前表类型{}尚未实现分析：{}", table.getClass().getName(), SQLUtils.toSQLString(table));
        }
        return new TableInfo();
    }
}
